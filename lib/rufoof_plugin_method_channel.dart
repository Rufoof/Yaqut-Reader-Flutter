import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:rufoof_plugin/constants/constants.dart';
import 'package:rufoof_plugin/models/rufoof_book.dart';
import 'package:rufoof_plugin/models/rufoof_reader_style.dart';

import 'rufoof_plugin_platform_interface.dart';

/// An implementation of [RufoofPluginPlatform] that uses method channels.
class MethodChannelRufoofPlugin extends RufoofPluginPlatform {
  /// The method channel used to interact with the native platform.

  @override
  Future<String?> getPlatformVersion() async {
    return '';
  }
}
