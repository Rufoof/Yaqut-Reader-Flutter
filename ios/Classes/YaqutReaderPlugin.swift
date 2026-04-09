import Flutter
import UIKit
import YaqutReader

public class YaqutReaderPlugin: NSObject, FlutterPlugin {
    var readerBuilder: ReaderBuilder?
    var channel: FlutterMethodChannel?
    var bookId: Int?

    public static func register(with registrar: FlutterPluginRegistrar) {
        let instance = YaqutReaderPlugin()
        instance.setAppearnce()
        instance.channel = FlutterMethodChannel(name: "yaqut_reader_plugin", binaryMessenger: registrar.messenger())
        registrar.addMethodCallDelegate(instance, channel: instance.channel!)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "getPlatformVersion":
            result("iOS " + UIDevice.current.systemVersion)
        case "startReader":
            if let arguments = call.arguments as? [String: Any],
               let book = arguments["book"] as? [String: Any],
               let style = arguments["style"] as? [String: Any] {
                let header = arguments["header"] as? String
                let path = arguments["path"] as? String
                let token = arguments["access_token"] as? String
                let saved = arguments["saved"] as? String
                let isDarkMode = arguments["is_dark_mode"] as? Bool ?? false
                self.startReader(header: header, path: path, accessToken: token, bookData: book, style: style, saved: saved == nil ? "disabled" : saved!, isDarkMode: isDarkMode)
            }
        case "checkIfLocal":
            if let arguments = call.arguments as? [String: Any] {
                if let bookId = arguments["book_id"] as? Int, let bookFileId = arguments["book_file_id"] as? Int {
                    let bookStorage = BookStorage()
                    let isLocal = bookStorage.isBookLocal(bookId: bookId)
                    result(isLocal)
                    return
                }
                result("AppDelegate Falied response")
            }
        case "checkIfSample":
            if let arguments = call.arguments as? [String: Any] {
                if let bookId = arguments["book_id"] as? Int {
                    let bookStorage = BookStorage()
                    let bookInfo = bookStorage.getBookInfo(bookId: bookId)
                    result(bookInfo.isSample)
                    return
               }
                result("AppDelegate Falied response")
           }

           case "getBookLength":
               if let arguments = call.arguments as? [String: Any] {
                   if let bookId = arguments["book_id"] as? Int {
                       let bookStorage = BookStorage()
                       let bookInfo = bookStorage.getBookInfo(bookId: bookId)
                       result(bookInfo.length)
                       return
                   }
                   result(0)
               } else {
                   result(0)
               }
        case "deleteSampleBook":
            if let arguments = call.arguments as? [String: Any] {
                if let bookId = arguments["book_id"] as? Int {
                    let bookStorage = BookStorage()
                    let success = bookStorage.deleteBook(bookId: bookId)
                    result(success)
                    return
                }
                result("AppDelegate Falied response")
            }
        case "getLocalBooks":
            let bookStorage = BookStorage()
            let localBooks = bookStorage.getLocalBooks()
            result(localBooks)
            return
        case "removeAllBooks":
            let bookStorage = BookStorage()
            bookStorage.removeAllBooks()
            return
        case "getLocalBooksInfo":
            let bookStorage = BookStorage()
            let filesInfo = bookStorage.checkDeviceFreeSpace()
            let serializedFilesInfo = filesInfo.map { fileInfo in
                return [
                    "id": fileInfo.id,
                    "size": fileInfo.size
                ] as [String: Any]
            }
            result(serializedFilesInfo)
            return
        case "hideReader":
            self.readerBuilder?.hideReaderView()
            return
        case "showReader":
            let showStart = CFAbsoluteTimeGetCurrent()
            self.readerBuilder?.showReaderView()
            return
        case "closeReader":
             self.readerBuilder?.closeBook()
             return
        case "updateMarks":
            if let arguments = call.arguments as? [String: Any], let marks = arguments["marks"] as? [[String: Any]] {
                self.updateMarks(notesAndMarksData: marks)
            }
            return
        case "updateDownloadProgress":
            handleUpdateDownloadProgress(call: call, result: result)
            return
        case "offlineDownloadComplete":
            if let arguments = call.arguments as? [String: Any], let bookId = arguments["book_id"] as? Int {
                self.offlineDownloadComplete(bookId: bookId)
            }
            return
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    // MARK: - Download Progress Bridge (Flutter -> Native UI)

    private func handleUpdateDownloadProgress(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let arguments = call.arguments as? [String: Any],
              let bookId = arguments["book_id"] as? Int,
              let progress = arguments["progress"] as? Double,
              let state = arguments["state"] as? String else {
            result(FlutterError(code: "INVALID_ARGUMENTS", message: "book_id, progress, and state are required", details: nil))
            return
        }

        let error = arguments["error"] as? String


        DispatchQueue.main.async { [weak self] in
            // Update native iOS reader's progress UI
            switch state {
            case "started":
                self?.readerBuilder?.onStartDownloadLoading()
            case "downloading":
                self?.readerBuilder?.onUpdateDownloadProgress(bookId: bookId, progress: Float(progress))
            case "completed":
                self?.readerBuilder?.onHideDownloadProgress()
                self?.readerBuilder?.onStopDownloadLoading()
            case "failed", "cancelled":
                self?.readerBuilder?.onHideDownloadProgress()
                self?.readerBuilder?.onStopDownloadLoading()
            default:
                break
            }
        }

        result(nil)
    }

    private func updateMarks(notesAndMarksData: [[String: Any]]) {
        let notesAndMarks = parseMarksFromFlutter(notesAndMarksData)
        self.readerBuilder?.updateMarks(allMarks: notesAndMarks)
    }

    private func parseMarksFromFlutter(_ data: [[String: Any]]) -> [NotesAndMarks] {
        return data.map { item in
            let newItem: [String: Any] = [
                "bookId": bookId ?? 0,
                "markId": item["id"] as? Int ?? 0,
                "fromOffset": item["location"] as? Int ?? 0,
                "toOffset": item["length"] as? Int ?? 0,
                "markColor": item["color"] as? Int ?? 0,
                "displayText": item["note"] as? String ?? "",
                "type": item["type"] as? Int ?? 0,
                "deleted": item["deleted"] as? Int ?? 0,
                "local": 1
            ]
            return NotesAndMarks(data: newItem)
        }
    }

    private func offlineDownloadComplete(bookId: Int) {
            let saveBookManager = SaveBookManager(bookId: bookId, bodyPath: "", header: nil, token: nil)
            saveBookManager.save()
        }

    private func startReader(header: String?, path: String?, accessToken: String?, bookData: [String: Any], style: [String: Any], saved: String, isDarkMode: Bool) {
        let startTime = CFAbsoluteTimeGetCurrent()

        let bookId = bookData["bookId"] as? Int ?? 0
        let bookFileId = bookData["bookFileId"] as? Int ?? 0
        let title = bookData["title"] as? String ?? ""
        let previewPercentage = bookData["previewPercentage"] as? Double ?? 0.15
        let position = bookData["position"] as? Int ?? 0
        self.bookId = bookId

        self.readerBuilder = ReaderBuilder(bookId: bookId, language: Language.arabic)
        self.readerBuilder?.setDarkMode(isDark: isDarkMode)

        self.readerBuilder?.setReaderDelegate(withReaderDelegate: self)
        self.readerBuilder?.setReadingStatsDelegate(withStatsSessionDelegate: self)
        self.readerBuilder?.setMiniPlayerMargin(miniPlayerMargin: 77)
        self.readerBuilder?.setTitle(bookTitle: title)
        self.readerBuilder?.setFileId(fileId: bookFileId)
        if let coverUrl = bookData["coverThumbUrl"] as? String {
            self.readerBuilder?.setCover(coverURL: coverUrl)
        }
        self.readerBuilder?.setPosition(startPosition: position)
        self.readerBuilder?.setPercentageView(previewPercentage: previewPercentage)
        self.readerBuilder?.setDownloadEnabled(downloadEnabled: true)

        if saved == "true" {
            self.readerBuilder?.setSaveState(saveState: .saved)
        } else if saved == "false" {
            self.readerBuilder?.setSaveState(saveState: .notSaved)
        } else {
            self.readerBuilder?.setSaveState(saveState: .disabled)
        }
        let notesAndMarksData = bookData["notesAndMarks"] as? [[String: Any]] ?? []
        let notesAndMarks = parseMarksFromFlutter(notesAndMarksData)
        self.readerBuilder?.setMarks(allMarks: notesAndMarks)

        let readerColor = style["readerColor"] as? Int ?? 0
        let textSize = style["textSize"] as? Int ?? 22
        let isJustified = style["isJustified"] as? Bool ?? true
        let lineSpacingValue = style["lineSpacing"] as? Int ?? 1
        let lineSpacing = LineSpacing(rawValue: lineSpacingValue) ?? LineSpacing.medium
        let font = style["font"] as? Int ?? 0
        let readerStyle = ReaderStyle(readerColor: readerColor, textSize: textSize, isJustified: isJustified, lineSpacing: lineSpacing, font: font)
        self.readerBuilder?.setReaderStyle(readerStyle: readerStyle)

        if (path ?? "") == "" {
            self.readerBuilder?.build()
            return
        }

        let saveBookManager = SaveBookManager(bookId: bookId, bodyPath: path ?? "", header: header == "" ? nil : header, token: accessToken == "" ? nil : accessToken)
        let saveBook = saveBookManager.save()

        if saveBook {
            self.readerBuilder?.build()
        }
    }

    private func setAppearnce() {

        UITableViewHeaderFooterView.appearance().backgroundColor = .blue

        UITabBar.appearance().tintColor = UIColor(red: 0.843, green: 0, blue: 0.212, alpha: 1)
        UITabBar.appearance().unselectedItemTintColor = UIColor.darkGray
        UITabBar.appearance().barTintColor = UIColor.white
        UITabBar.appearance().backgroundColor = UIColor.white

        let coconDescriptor = UIFontDescriptor(fontAttributes: [UIFontDescriptor.AttributeName.family: "Tajawal", UIFontDescriptor.AttributeName.face: "Regular"])
        let tajaDescriptor = UIFontDescriptor(fontAttributes: [UIFontDescriptor.AttributeName.family: "CoconÆ Next Arabic", UIFontDescriptor.AttributeName.face: "Regular"])

        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedString.Key.font: UIFont(descriptor: tajaDescriptor, size: 14.0)], for: .normal)

        UIBarButtonItem.appearance(whenContainedInInstancesOf: [UISearchBar.self]).setTitleTextAttributes([NSAttributedString.Key.foregroundColor: UIColor(red: 0.071, green: 0.071, blue: 0.071, alpha: 1), NSAttributedString.Key.font: UIFont(descriptor: coconDescriptor, size: 15)], for: .normal)
        UIBarButtonItem.appearance(whenContainedInInstancesOf: [UISearchBar.self]).title = "إلغاء"

        UINavigationBar.appearance().tintColor = UIColor(red: 0.071, green: 0.071, blue: 0.071, alpha: 1)
        UINavigationBar.appearance().barTintColor = UIColor.white
        UINavigationBar.appearance().backgroundColor = UIColor.white
        UINavigationBar.appearance().isTranslucent = false
        UINavigationBar.appearance().titleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor(red: 0.071, green: 0.071, blue: 0.071, alpha: 1), NSAttributedString.Key.font: UIFont(descriptor: coconDescriptor, size: 20.0)]
        if #available(iOS 13.0, *) {
            let app = UINavigationBarAppearance()
            app.backgroundColor = UIColor.white
            app.titleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor(red: 0.071, green: 0.071, blue: 0.071, alpha: 1), NSAttributedString.Key.font: UIFont(descriptor: coconDescriptor, size: 20.0)]
            let img = UIImage(systemName: "chevron.forward")?.withTintColor(UIColor(red: 0.071, green: 0.071, blue: 0.071, alpha: 1), renderingMode: .alwaysOriginal)
            app.setBackIndicatorImage(img, transitionMaskImage: img)
            UINavigationBar.appearance().standardAppearance = app
            UINavigationBar.appearance().scrollEdgeAppearance = app
            UINavigationBar.appearance().compactAppearance = app
        }

        UISearchBar.appearance().semanticContentAttribute = .forceRightToLeft
        UISearchBar.appearance().barTintColor = UIColor.white
        UISearchBar.appearance().backgroundColor = UIColor.white
        UISearchBar.appearance().searchBarStyle = .minimal
        UISearchBar.appearance().showsCancelButton = true

        UIView.appearance().semanticContentAttribute = .forceRightToLeft
    }
}

extension YaqutReaderPlugin: ReaderDelegate {
    public func onStyleChanged(style: ReaderStyle) {
        let linespace = style.lineSpacing.rawValue
        let readerColor = style.readerColor
        let fontIndex = style.font
        let fontSize = style.textSize
        let layout = style.isJustified ? 1 : 2
        let data: [String: Int] = [
             "line_space": linespace,
             "reader_color": readerColor,
             "font": fontIndex,
             "font_size": fontSize,
             "layout": layout,
             "book_id": self.bookId ?? 0
         ]
         channel?.invokeMethod("onStyleChanged", arguments: data)
    }

    public func onPositionChanged(position: Int) {
        let data:[String: Int] = ["position": position, "book_id": self.bookId ?? 0]
        channel?.invokeMethod("onPositionChanged", arguments: data)
    }

    public func onBookDetailsCLicked() {
        channel?.invokeMethod("onBookDetailsClicked", arguments: [:])
    }

    public func onSaveBookClicked(position: Int) {
        let data:[String: Int] = ["position": position, "book_id": self.bookId ?? 0]
        channel?.invokeMethod("onSaveBookClicked", arguments: data)
    }

    public func onShareBook() {
        channel?.invokeMethod("onShareBook", arguments: [:])
    }

    public func onShareQuotes(text: String) {
        channel?.invokeMethod("onShareQuotes", arguments: ["text": text])
    }

    public func onDownloadBook() {
        channel?.invokeMethod("onDownloadBook", arguments: [:])
    }

    public func onSyncNotesAndMarks(list: [YaqutReader.NotesAndMarks]) {
        var items = [[String: Any]]()
        for mark in list {
            let item: [String: Any] = [
                "book_id": self.bookId ?? 0, "mark_id": mark.markId ?? 0,
                "from_offset": mark.fromOffset,
                "to_offset": mark.toOffset, "mark_color": mark.markColor ?? 0,
                "display_text": mark.displayText ?? "", "type": mark.type,
                "deleted": mark.deleted ?? 0, "local": mark.local ?? 1
            ]
            items.append(item)
        }

        channel?.invokeMethod("onSyncNotes", arguments: items)
    }

    public func onReaderClosed(position: Int) {
        let data:[String: Int] = ["position": position, "book_id": self.bookId ?? 0]
        channel?.invokeMethod("onReaderClosed", arguments: data)
        self.readerBuilder?.closeBook()
        self.readerBuilder = nil
    }

    public func onSampleEnded() {
        channel?.invokeMethod("onSampleEnded", arguments: [:])
    }

    public func onOrientationChanged() {
        channel?.invokeMethod("onOrientationChanged", arguments: [:])
    }
}

extension YaqutReaderPlugin: StatsSessionDelegate {
    public func onReadingSessionEnd(session: YaqutReader.RRReadingSession) {
        let data:[String: Any] = [
            "book_id": session.bookId,
            "book_file_id": session.bookFileId,
            "pages_read": session.pagesRead,
            "start_offset": session.startOffset,
            "end_offset": session.endOffset,
            "covered_offset": session.coveredOffset,
            "covered_length": session.coveredLength,
            "start_time": session.startTime,
            "end_time": session.endTime,
            "md5": session.md5,
            "uuid": session.uuid ?? ""
            ]
        channel?.invokeMethod("onReadingSessionEnd", arguments: data)
    }
}
