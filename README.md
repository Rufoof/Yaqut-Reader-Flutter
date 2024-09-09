
# YaqutReaderSDK

## Installation
1. Open `pubspec.yaml` file.
2. Under `dependencies` add the following:

```dart
rufoof_plugin:
    git:
      url: https://github.com/Rufoof/Reader-Flutter-Framework.git
      ref: master
```


### Usage

### Sample books

1. Create an instance from `YaqutReaderPlugin`:
    ```dart
    YaqutReaderPlugin yaqutReaderPlugin = YaqutReaderPlugin();
    ```

2. Check if book already downloaded by calling `checkIfLocal` with arguments `book_id, book_file_id`:
    ```dart
    bool isLocal = await yaqutReaderPlugin.checkIfLocal(bookId, fileId);
    ```

3. If you received the `isLocal` value as `false`,
    you need to download the sample book file and save it in the document directory.

    **Note:** it must be named as ‘book_id.yaqut’, for example, ‘123456.yaqut’

    ```dart
    Uint8List bookData = await downloadFile(fileUrl);
    String path = await writeSampleBook(bookData, bookId);
    SaveBookManager saveBookManager = SaveBookManager(bookId)
    bool isSavedSuccessfully = saveBookManager.save()
    .
    .
    .
    .

    Future<Uint8List> downloadFile(String url) async {
        final response = await http.get(Uri.parse(url));
        if (response.statusCode == 200) {
            return response.bodyBytes;
        } else {
            throw Exception('Failed to download file');
        }
    }

    Future<String> writeSampleBook(Uint8List data, int bookId) async {
        final Directory appDocDir = await getApplicationDocumentsDirectory();
        final Directory filePath = Directory('${appDocDir.path}/${bookId}.yaqut');
        try {
            final file = File(filePath.path);
            await file.writeAsBytes(data);
            return filePath.path;
        } catch (e) {
            if (kDebugMode) {
                print('Error writing file: $e');
            }
        return "";
        }
    }
    ```

    3.1 Create `YaqutReaderStyle` object

    3.2 Create `YaqutBook` object

    you need to pass `YaqutReaderStyle`, `YaqutBook` and the book `path`
    ```dart
    if (isSavedSuccessfully) {
        yaqutReaderPlugin.startReader(
        header: null,
        path: path,
        accessToken: null,
        book: book,
        style: style);
    }
    ``` 

4. If you received the `is_local` value as `true`, you need to do the following:

    4.1 Create `YaqutReaderStyle` object

    4.2 Create `YaqutBook` object

    you need to pass `book` and `style`
    ```dart
        yaqutReaderPlugin.startReader(
        header: null,
        path: null,
        accessToken: null,
        book: book,
        style: style);
    ```

### Full books

1. Create an instance from `YaqutReaderPlugin`:
    ```dart
    YaqutReaderPlugin yaqutReaderPlugin = YaqutReaderPlugin();
    ```

2. Check if book already downloaded by calling `checkIfLocal` with arguments `book_id, book_file_id`:
    ```dart
    bool isLocal = await yaqutReaderPlugin.checkIfLocal(bookId, fileId);
    ```

3. If you received the `isLocal` value as `false`,
    you need to download the full book file and save it in the document directory.

    **Note:** it must be named as ‘book_id.yaqut’, for example, ‘123456.yaqut’

    ```dart
    JSON json = await downloadHeaderBodyUrl(boolFileId);
    String bodyUrl = json.body;
    String header = json.header;

    Uint8List bodyData = await downloadFile(bodyUrl);
    String path = await writeBody(bookData);
    .
    .
    .
    .

    Future<JSON> downloadHeaderBodyUrl(int boolFileId) async {
        final response = await http.get(Uri.parse(url));
        if (response.statusCode == 200) {
            return response;
        } else {
            throw Exception('Failed to downloadHeaderBodyUrl');
        }
    }

    Future<Uint8List> downloadFile(String url) async {
        final response = await http.get(Uri.parse(url));
        if (response.statusCode == 200) {
            return response.bodyBytes;
        } else {
            throw Exception('Failed to download file');
        }
    }

    Future<String> writeBody(Uint8List data) async {
        final Directory appDocDir = await getApplicationDocumentsDirectory();
        final Directory filePath = Directory('${appDocDir.path}/body');
        try {
            final file = File(filePath.path);
            await file.writeAsBytes(data);
            return filePath.path;
        } catch (e) {
            if (kDebugMode) {
                print('Error writing file: $e');
            }
        return "";
        }
    }
    ```

    3.1 Create `YaqutReaderStyle` object

    3.2 Create `YaqutBook` object

    3.3 Obtain the user `accessToken`

    you need to pass `YaqutReaderStyle`, `YaqutBook` and the book `path`
    ```dart
        yaqutReaderPlugin.startReader(
        header: null,
        path: path,
        accessToken: accessToken,
        book: book,
        style: style);
    ``` 

4. If you received the `is_local` value as `true`, you need to do the following:

    4.1 Create `YaqutReaderStyle` object

    4.2 Create `YaqutBook` object

    you need to pass `book` and `style`
    ```dart
        yaqutReaderPlugin.startReader(
        header: null,
        path: null,
        accessToken: null,
        book: book,
        style: style);
    ```

### Callbacks

6. From `yaqutReaderPlugin` Check the following callbacks:
    `onStyleChanged`, `onPositionChanged`, `onBookDetailsCLicked`, `onShareBook`,
    `onSaveBookClicked`, `onDownloadBook`, `onSyncNotes`, `onSyncReadingSession`

    ```dart
    yaqutReaderPlugin.onStyleChanged.listen((style) {
      // save style
    });

    yaqutReaderPlugin.onPositionChanged.listen((position) {
      // save position
    });

    yaqutReaderPlugin.onBookDetailsCLicked.listen((msg) {
      // open book details
    });

    yaqutReaderPlugin.onShareBook.listen((msg) {
      // share book
    });

    yaqutReaderPlugin.onSaveBookClicked.listen((msg) {
      if (kDebugMode) {
        print(msg);
      }
    });

    yaqutReaderPlugin.onDownloadBook.listen((msg) {
      // download offline version
    });

    yaqutReaderPlugin.onSyncNotes.listen((notes) {
      // save notes
    });

    yaqutReaderPlugin.onSyncReadingSession.listen((readingSession) {
      // save reading session
    });
    ```
