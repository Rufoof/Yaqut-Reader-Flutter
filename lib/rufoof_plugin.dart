import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:rufoof_plugin/constants/constants.dart';
import 'package:rufoof_plugin/models/rufoof_book.dart';
import 'package:rufoof_plugin/models/rufoof_reader_style.dart';

class RufoofPlugin {
  final methodChannel = const MethodChannel('rufoof_plugin');
  final StreamController<MethodCall> callbackStreamController =
      StreamController<MethodCall>.broadcast();
  Stream<MethodCall> get callbackStream => callbackStreamController.stream;

  void sendCallback(MethodCall data) {
    callbackStreamController.add(data);
  }

  Future<void> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required RufoofBook book,
      required RufoofReaderStyle style}) async {
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
          "$constReaderPluginTag readerListener Called method: ${call.method}");
    }
    sendCallback(call);
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

  getPlatformVersion() {}
}
