import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:yaqut_reader_plugin/constants/constants.dart';
import 'package:yaqut_reader_plugin/models/yaqut_reader_book.dart';
import 'package:yaqut_reader_plugin/models/yaqut_reader_note.dart';
import 'package:yaqut_reader_plugin/models/yaqut_reader_reading_session.dart';
import 'package:yaqut_reader_plugin/models/yaqut_reader_style.dart';

class YaqutReaderPlugin {
  final methodChannel = const MethodChannel('yaqut_reader_plugin');

  final StreamController<YaqutReaderStyle> onStyleChangedStreamController =
      StreamController<YaqutReaderStyle>.broadcast();
  final StreamController<int> onPositionChangedStreamController =
      StreamController<int>.broadcast();
  final StreamController<List<YaqutReaderNote>> onSyncNotesStreamController =
      StreamController<List<YaqutReaderNote>>.broadcast();
  final StreamController<String> onBookDetailsCLickedStreamController =
      StreamController<String>.broadcast();
  final StreamController<String> onSaveBookClickedStreamController =
      StreamController<String>.broadcast();
  final StreamController<String> onDownloadBookStreamController =
      StreamController<String>.broadcast();
  final StreamController<String> onShareBookStreamController =
      StreamController<String>.broadcast();
  final StreamController<int> onReaderClosedStreamController =
      StreamController<int>.broadcast();
  final StreamController<String> onSampleEndedStreamController =
      StreamController<String>.broadcast();
  final StreamController<YaqutReaderReadingSession>
      onSyncReadingSessionStreamController =
      StreamController<YaqutReaderReadingSession>.broadcast();

  Stream<YaqutReaderStyle> get onStyleChanged =>
      onStyleChangedStreamController.stream;
  Stream<int> get onPositionChanged => onPositionChangedStreamController.stream;
  Stream<List<YaqutReaderNote>> get onSyncNotes =>
      onSyncNotesStreamController.stream;
  Stream<String> get onBookDetailsCLicked =>
      onBookDetailsCLickedStreamController.stream;
  Stream<String> get onSaveBookClicked =>
      onSaveBookClickedStreamController.stream;
  Stream<String> get onShareBook => onShareBookStreamController.stream;
  Stream<String> get onDownloadBook => onDownloadBookStreamController.stream;
  Stream<int> get onReaderClosed => onReaderClosedStreamController.stream;
  Stream<String> get onSampleEnded => onSampleEndedStreamController.stream;
  Stream<YaqutReaderReadingSession> get onSyncReadingSession =>
      onSyncReadingSessionStreamController.stream;

  void onStyleChangedCallback(YaqutReaderStyle style) {
    onStyleChangedStreamController.add(style);
  }

  void onPositionChangedCallback(int position) {
    onPositionChangedStreamController.add(position);
  }

  void onSyncNotesCallback(List<YaqutReaderNote> notes) {
    onSyncNotesStreamController.add(notes);
  }

  void onBookDetailsCLickedCallback() {
    onBookDetailsCLickedStreamController.add('onBookDetailsCLicked');
  }

  void onSaveBookClickedCallback() {
    onSaveBookClickedStreamController.add('onSaveBookClicked');
  }

  void onShareBookCallback() {
    onShareBookStreamController.add('onShareBook');
  }

  void onDownloadBookCallback() {
    onDownloadBookStreamController.add('onDownloadBook');
  }

  void onReaderClosedCallback(int position) {
    onReaderClosedStreamController.add(position);
  }

  void onSampleEndedCallback() {
    onSampleEndedStreamController.add('onSampleEnded');
  }

  void onSyncReadingSessionCallback(YaqutReaderReadingSession session) {
    onSyncReadingSessionStreamController.add(session);
  }

  Future<void> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required YaqutReaderBook book,
      required YaqutReaderStyle style}) async {
    methodChannel.setMethodCallHandler(readerListener);
    try {
      await methodChannel.invokeMethod('startReader', {
        constHeader: header,
        constPath: path,
        constAccessToken: accessToken,
        constBook: book.toJson(),
        constStyle: style.toJson()
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        debugPrint("Failed to call native method: '${e.message}'.");
      }
    }
  }

  Future<void> readerListener(MethodCall call) async {
    if (kDebugMode) {
      debugPrint(
          "$constYaqutReaderPluginTag readerListener Called method: ${call.method}");
    }
    switch (call.method) {
      case 'onStyleChanged':
        var data = call.arguments as Map;
        var lineSpace = data[constLineSpace];
        var layout = data[constLayout];
        var fontSize = data[constFontSize];
        var font = data[constFont];
        var readerColor = data[constReaderColor];
        YaqutReaderStyle style = YaqutReaderStyle(
            readerColor: readerColor,
            textSize: fontSize,
            isJustified: layout == 1 ? true : false,
            lineSpacing: lineSpace,
            font: font);
        onStyleChangedCallback(style);
        if (kDebugMode) {
          debugPrint("...onStyleChangedCallback...");
        }
      case 'onPositionChanged':
        var data = call.arguments as Map;
        int position = data[constPosition];
        onPositionChangedCallback(position);
      case 'onBookDetailsCLicked':
        onBookDetailsCLickedCallback();
      case 'onSaveBookClicked':
        onSaveBookClickedCallback();
      case 'onShareBook':
        onShareBookCallback();
      case 'onDownloadBook':
        onDownloadBookCallback();
      case 'onSyncNotes':
        List<YaqutReaderNote> notes = [];
        final List<dynamic> marks = call.arguments;
        for (var mark in marks) {
          var note = YaqutReaderNote.fromJson(mark);
          notes.add(note);
        }
        onSyncNotesCallback(notes);
      case 'onReaderClosed':
        var data = call.arguments as Map;
        int position = data[constPosition];
        onReaderClosedCallback(position);
      case 'onSampleEnded':
        onSampleEndedCallback();
      case 'onReadingSessionEnd':
        final Map<Object?, Object?> rawData =
            call.arguments as Map<Object?, Object?>;
        final Map<String, dynamic> data = rawData.map(
          (key, value) => MapEntry(key as String, value),
        );
        YaqutReaderReadingSession session =
            YaqutReaderReadingSession.fromJson(data);
        onSyncReadingSessionCallback(session);
      default:
    }
  }

  Future<bool> checkIfLocal(int bookId, int bookFileId) async {
    bool? isLocal = false;
    try {
      isLocal = await methodChannel.invokeMethod<bool>('checkIfLocal', {
        'book_id': bookId,
        'book_file_id': bookFileId,
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        debugPrint("Failed to call native method: '${e.message}'.");
      }
    }
    return isLocal!;
  }

  Future<bool> moveOfflineFile(int bookId) async {
    bool? success = false;
    try {
      success = await methodChannel.invokeMethod<bool>('moveOfflineFile', {
        'book_id': bookId,
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        debugPrint("Failed to call native method: '${e.message}'.");
      }
    }
    return success!;
  }

  getPlatformVersion() {}
}
