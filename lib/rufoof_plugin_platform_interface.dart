import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:rufoof_plugin/models/rufoof_book.dart';
import 'package:rufoof_plugin/models/rufoof_reader_style.dart';

import 'rufoof_plugin_method_channel.dart';

abstract class RufoofPluginPlatform extends PlatformInterface {
  /// Constructs a RufoofPluginPlatform.
  RufoofPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static RufoofPluginPlatform _instance = MethodChannelRufoofPlugin();

  /// The default instance of [RufoofPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelRufoofPlugin].
  static RufoofPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [RufoofPluginPlatform] when
  /// they register themselves.
  static set instance(RufoofPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
