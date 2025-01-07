import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'yaqut_reader_plugin_method_channel.dart';

abstract class YaqutReaderPluginPlatform extends PlatformInterface {
  /// Constructs a YaqutReaderPluginPlatform.
  YaqutReaderPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static YaqutReaderPluginPlatform _instance = MethodChannelYaqutReaderPlugin();

  /// The default instance of [YaqutReaderPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelYaqutReaderPlugin].
  static YaqutReaderPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [YaqutReaderPluginPlatform] when
  /// they register themselves.
  static set instance(YaqutReaderPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
