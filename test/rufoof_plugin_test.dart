import 'package:flutter_test/flutter_test.dart';
import 'package:rufoof_plugin/models/book.dart';
import 'package:rufoof_plugin/models/reader_style.dart';
import 'package:rufoof_plugin/rufoof_plugin.dart';
import 'package:rufoof_plugin/rufoof_plugin_platform_interface.dart';
import 'package:rufoof_plugin/rufoof_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockRufoofPluginPlatform
    with MockPlatformInterfaceMixin
    implements RufoofPluginPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<void> startReader(
      {required String? header,
      required String? path,
      required String? accessToken,
      required Book? book,
      required ReaderStyle? style}) {
    // TODO: implement startReader
    throw UnimplementedError();
  }
}

void main() {
  final RufoofPluginPlatform initialPlatform = RufoofPluginPlatform.instance;

  test('$MethodChannelRufoofPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelRufoofPlugin>());
  });

  test('getPlatformVersion', () async {
    RufoofPlugin rufoofPlugin = RufoofPlugin();
    MockRufoofPluginPlatform fakePlatform = MockRufoofPluginPlatform();
    RufoofPluginPlatform.instance = fakePlatform;

    expect(await rufoofPlugin.getPlatformVersion(), '42');
  });
}
