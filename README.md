
# YaqutReaderSDK
## Install reader locally:
```bash
./gradlew -p publishing publish
```

## Installation
1. Open `pubspec.yaml` file.
2. Under `dependencies` add the following:

```dart
yaqut_reader_plugin:
    git:
      url: https://github.com/Rufoof/Yaqut-Reader-Flutter.git
      ref: 1.1.0
```


3. open `android/build.gradle`
4. access the local maven
```build.gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal() // Ensures the project can access the locally published AAR
    }
```
### Usage

You need to create 'Book' object and call 'startReader' from 'UserBooks'
    .

    ```dart
    retrieve.Book newBook = UserBooks.userBookDtoToRetrieveBook(book);
    await UserBooks.startReader(newBook);
    ```  

### Callbacks

From `yaqutReaderPlugin` Check the following callbacks:
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
