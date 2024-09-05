
# RufoofReaderSDK

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
1. Create an instance from `RufoofPlugin`:
    ```dart
    RufoofPlugin rufoofPlugin = RufoofPlugin();
    ```

2. Check if book already downloaded by calling `checkIfLocal` with arguments `book_id, book_file_id`:
    ```dart
    bool isLocal = await rufoofPlugin.checkIfLocal(bookId, fileId);
    ```

3. If you received the `isLocal` value as `false`,
    you need to download the book sample file and save it in the document directory for iOS.

    **Note:** it must be named as ‘book_id.yaqut’, for example, ‘123456.yaqut’

    3.1 Create `RufoofReaderStyle` object

    3.2 Create `RufoofBook` object

    you need to pass `RufoofReaderStyle`, `RufoofBook` and the book `path`
    ```dart
        RufoofPlugin rufoofPlugin = RufoofPlugin();
        rufoofPlugin.startReader(
        header: null,
        path: path,
        accessToken: null,
        book: book,
        style: style);
    ``` 

4. If you received the `is_local` value as `true`, you need to do the following:

    4.1 Create `RufoofReaderStyle` object

    4.2 Create `RufoofBook` object

    you need to pass `book` and `style`
    ```dart
        RufoofPlugin rufoofPlugin = RufoofPlugin();
        rufoofPlugin.startReader(
        header: null,
        path: null,
        accessToken: null,
        book: book,
        style: style);
    ```

6. On the `callbackStream` check the following callbacks
    `onPositionChanged`, `onReaderClosed`, `onStyleChanged`, `onSyncNotes`, `onSampleEnded`,
    `onBookDetailsCLicked`, `onSaveBookClicked`, `onShareBook`, `onDownloadBook`, `onReadingSessionEnd`

    ```dart
        rufoofPlugin.callbackStream.listen((data) {
      switch (data.method) {
        case 'onPositionChanged':
            var data = data.arguments as Map;
            int position = data[constPosition];
            int bookId = data[constBookId];
        case 'onReaderClosed':
            ...
        case 'onStyleChanged':
            ...
        case 'onSyncNotes':
            List<Map<String, dynamic>> rows = [];
            final List<dynamic> marks = call.arguments;
            for (var mark in marks) {
                var bookId = mark[constBookId];
                var fromOffset = mark[constFromOffset];
                var toOffset = mark[constToOffset];
                var markColor = mark[constMarkColor];
                var displayText = mark[constDisplayText];
                var type = mark[constType];
                var deleted = mark[constDeleted];
                var local = mark[constLocal];
            }
        default:
          if (kDebugMode) {
            debugPrint('Unknown method');
          }
      }
    });
    ```

## Contributing
Information on how to contribute to the project.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
