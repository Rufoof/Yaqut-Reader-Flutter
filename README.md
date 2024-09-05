
# RufoofReaderSDK

## Overview


### Usage
1. Create MethodChannel named `rufoof_plugin`:
    ```dart
    static const MethodChannel channel = MethodChannel('rufoof_plugin');
    ```

2. Check if book already downloaded by calling invokeMethod `checkIfLocal` with arguments `book_id, book_file_id`:
    ```dart
    channel.invokeMethod('checkIfLocal', {'book_id': book_id, 'book_file_id': book_file_id});
    ```

3. On the `readerListener` check the call method `onLocalChecked` to get the result:
    ```dart
        var data = call.arguments as Map;
        bool isLocal = data['is_local'];
        int bookId = data['book_id'];
        int bookFileId = data['book_file_id'];
    ```

4. If you received the `is_local` value as `false`,
    you need to download the book sample file and save it in the document directory for iOS.
    **Note:** it must be named as ‘book_id.yaqut’, for example, ‘123456.yaqut’
    4.1 Create `readerStyle` as `Map<String, dynamic>`
    ```dart
        Map<String, dynamic> style = {
            'readerColor': readerColor,
            'textSize': textSize,
            'isJustified': isJustified,
            'lineSpacing': lineSpacing,
            'font': font
        }
    ```

    4.2 Create `rufoofPlugin` from `RufoofPlugin` and call `startReader`.
    you need to pass `book`, `style` as `Map<String, dynamic>` and the book `path`
    ```dart
        RufoofPlugin rufoofPlugin = RufoofPlugin();
        rufoofPlugin.startReader(
        header: null,
        path: path,
        accessToken: null,
        book: book,
        style: style);
    ``` 

5. If you received the `is_local` value as `true`, you need to do the following:
    5.1 Create `readerStyle` as `Map<String, dynamic>`
    ```dart
        Map<String, dynamic> style = {
            'readerColor': readerColor,
            'textSize': textSize,
            'isJustified': isJustified,
            'lineSpacing': lineSpacing,
            'font': font
        }
    ```

    5.2 Create `rufoofPlugin` from `RufoofPlugin` and call `startReader`.
    you need to pass `book` and `style` as `Map<String, dynamic>`
    ```dart
        RufoofPlugin rufoofPlugin = RufoofPlugin();
        rufoofPlugin.startReader(
        header: null,
        path: null,
        accessToken: null,
        book: book,
        style: style);
    ```

6. On the `readerListener` check the following call methods
    `onPositionChanged`, `onReaderClosed`, `onStyleChanged`, `onSyncNotes`, `onSampleEnded`,
    `onBookDetailsCLicked`, `onSaveBookClicked`, `onShareBook`, `onDownloadBook`, `onReadingSessionEnd`

## Contributing
Information on how to contribute to the project.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
