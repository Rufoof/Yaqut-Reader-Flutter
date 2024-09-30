import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'yaqut_reader_flutter_platform_interface.dart';

/// An implementation of [YaqutReaderFlutterPlatform] that uses method channels.
class MethodChannelYaqutReaderFlutter extends YaqutReaderFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('yaqut_reader_flutter');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
