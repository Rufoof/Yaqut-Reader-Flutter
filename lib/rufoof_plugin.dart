import 'package:rufoof_plugin/models/book.dart';
import 'package:rufoof_plugin/models/reader_style.dart';

import 'rufoof_plugin_platform_interface.dart';

class RufoofPlugin {
  Future<String?> getPlatformVersion() {
    return RufoofPluginPlatform.instance.getPlatformVersion();
  }

  Future<void> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required Map<String, dynamic>? book,
      required Map<String, dynamic>? style}) {
    return RufoofPluginPlatform.instance.startReader(
        header: header,
        path: path,
        accessToken: accessToken,
        book: book,
        style: style);
  }
}
