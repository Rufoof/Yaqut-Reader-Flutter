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
                self.startReader(header: header, path: path, accessToken: token, bookData: book, style: style)
            }
        case "checkIfLocal":
            if let arguments = call.arguments as? [String: Any] {
                if let bookId = arguments["book_id"] as? Int, let bookFileId = arguments["book_file_id"] as? Int {
                    let bookStorage = BookStorage()
                    let isLocal = bookStorage.isBookLocal(bookId: bookId)
                    let data: [String: Any] = [
                        "is_local": isLocal,
                        "book_id": bookId,
                        "book_file_id": bookFileId,
                    ]
                    result(isLocal)
                    return
                }
                result("AppDelegate Falied response")
            }
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    
    private func startReader(header: String?, path: String?, accessToken: String?, bookData: [String: Any], style: [String: Any]) {
        let bookId = bookData["bookId"] as? Int ?? 0
        let bookFileId = currentFile["bookFileId"] as? Int ?? 0
        let title = bookData["title"] as? String ?? ""
        let previewPercentage = bookData["samplePreviewPercentage"] as? Double ?? 0.15
        let position = bookData["position"] as? Int ?? 0
        self.bookId = bookId
        self.readerBuilder = ReaderBuilder(bookId: bookId, language: Language.arabic)
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
        self.readerBuilder?.setSaveState(saveState: .NOT_SAVED)
        let notesAndMarksData = bookData["notesAndMarks"] as? [[String: Any]] ?? []
        var notesAndMarks = [NotesAndMarks]()
        for item in notesAndMarksData {
            let noteAndMark = NotesAndMarks(dbData: item)
            notesAndMarks.append(noteAndMark)
        }
        self.readerBuilder?.setMarks(allMarks: notesAndMarks)
        let readerColor = style["readerColor"] as? Int ?? 0
        let textSize = style["textSize"] as? Int ?? 22
        let isJustified = style["isJustified"] as? Bool ?? true
        let lineSpacingValue = style["lineSpacing"] as? Int ?? 1
        let lineSpacing = LineSpacing(rawValue: lineSpacingValue) ?? LineSpacing.LINESPACE_MEDIUM
        let font = style["font"] as? Int ?? 0
        let readerStyle = ReaderStyle(readerColor: readerColor, readerTextSize: textSize, isJustified: isJustified, lineSpacing: lineSpacing, font: font)
        self.readerBuilder?.setReaderStyle(readerStyle: readerStyle)
        guard let path = path else {
            self.readerBuilder?.build()
            return
        }
        
        let saveBookManager = SaveBookManager(bookId: bookId, bodyPath: path, header: header, token: accessToken)
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
        channel?.invokeMethod("onBookDetailsCLicked", arguments: [:])
    }

    public func onSaveBookClicked(position: Int) {
        let data:[String: Int] = ["position": position, "book_id": self.bookId ?? 0]
        channel?.invokeMethod("onSaveBookClicked", arguments: data)
    }

    public func onShareBook() {
        channel?.invokeMethod("onShareBook", arguments: [:])
    }

    public func onDownloadBook() {
        channel?.invokeMethod("onDownloadBook", arguments: [:])
    }

    public func onSyncNotesAndMarks(list: [YaqutReader.NotesAndMarks]) {
        var items = [[String: Any]]()
        for mark in list {
            let item: [String: Any] = [
                "book_id": self.bookId ?? 0, "from_offset": mark.fromOffset,
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
}

extension YaqutReaderPlugin: StatsSessionDelegate {
    public func onReadingSessionEnd(session: YaqutReader.RRReadingSession) {
        channel?.invokeMethod("onReadingSessionEnd", arguments: [:])
    }
}
