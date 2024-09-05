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

  Future<MethodCall?> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required RufoofBook book,
      required RufoofReaderStyle style}) async {
    MethodCall? method;
    try {
      method = await methodChannel.invokeMethod<MethodCall>('startReader', {
        constHeader: header,
        constPath: path,
        constAccessToken: accessToken,
        constBook: book.toJson(),
        constStyle: style.toJson()
      });

      methodChannel.setMethodCallHandler((MethodCall call) async {
        sendCallback(call);
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        debugPrint("Failed to call native method: '${e.message}'.");
      }
    }
    return method;
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
