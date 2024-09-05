import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:rufoof_plugin/constants/constants.dart';
import 'package:rufoof_plugin/models/rufoof_book.dart';
import 'package:rufoof_plugin/models/rufoof_reader_style.dart';

import 'rufoof_plugin_platform_interface.dart';

/// An implementation of [RufoofPluginPlatform] that uses method channels.
class MethodChannelRufoofPlugin extends RufoofPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('rufoof_plugin');

  @override
  void initialize({required Function(int, int) onPositionChanged}) {
    methodChannel.setMethodCallHandler((MethodCall call) async {
      switch (call.method) {
        case 'onPositionChanged':
          final Map<String, dynamic> data =
              Map<String, dynamic>.from(call.arguments);
          int position = data['position'] as int;
          int bookId = data['book_id'] as int;
          onPositionChanged(position, bookId);
          break;
        default:
          throw MissingPluginException(
              'Method ${call.method} not implemented.');
      }
    });
  }

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
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
    } on PlatformException catch (e) {
      if (kDebugMode) {
        debugPrint("Failed to call native method: '${e.message}'.");
      }
    }
    return method;
  }

  @override
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
}
