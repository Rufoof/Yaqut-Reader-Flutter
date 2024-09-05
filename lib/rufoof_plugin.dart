import 'package:rufoof_plugin/models/rufoof_book.dart';
import 'package:rufoof_plugin/models/rufoof_reader_style.dart';

import 'rufoof_plugin_platform_interface.dart';

class RufoofPlugin {
  Future<String?> getPlatformVersion() {
    return RufoofPluginPlatform.instance.getPlatformVersion();
  }

  Future<void> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required RufoofBook book,
      required RufoofReaderStyle style}) {
    return RufoofPluginPlatform.instance.startReader(
        header: header,
        path: path,
        accessToken: accessToken,
        book: book,
        style: style);
  }

  Future<void> checkIfLocal(int bookId, int bookFileId) {
    return RufoofPluginPlatform.instance.checkIfLocal(bookId, bookFileId);
  }
}
