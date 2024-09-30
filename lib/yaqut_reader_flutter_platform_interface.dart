import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'yaqut_reader_flutter_method_channel.dart';

abstract class YaqutReaderFlutterPlatform extends PlatformInterface {
  /// Constructs a YaqutReaderFlutterPlatform.
  YaqutReaderFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static YaqutReaderFlutterPlatform _instance = MethodChannelYaqutReaderFlutter();

  /// The default instance of [YaqutReaderFlutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelYaqutReaderFlutter].
  static YaqutReaderFlutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [YaqutReaderFlutterPlatform] when
  /// they register themselves.
  static set instance(YaqutReaderFlutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
