import 'dart:async';
import 'yaqut_reader_plugin_platform_interface.dart';

/// An implementation of [YaqutReaderPluginPlatform] that uses method channels.
class MethodChannelYaqutReaderPlugin extends YaqutReaderPluginPlatform {
  /// The method channel used to interact with the native platform.

  @override
  Future<String?> getPlatformVersion() async {
    return '';
  }
}
