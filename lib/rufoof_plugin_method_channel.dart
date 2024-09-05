import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:rufoof_plugin/constants/constants.dart';
import 'package:rufoof_plugin/models/book.dart';
import 'package:rufoof_plugin/models/reader_style.dart';

import 'rufoof_plugin_platform_interface.dart';

/// An implementation of [RufoofPluginPlatform] that uses method channels.
class MethodChannelRufoofPlugin extends RufoofPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('rufoof_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required Book book,
      required ReaderStyle style}) async {
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
}
