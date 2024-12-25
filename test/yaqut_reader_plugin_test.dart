import 'package:flutter_test/flutter_test.dart';
import 'package:yaqut_reader_plugin/yaqut_reader_plugin.dart';
import 'package:yaqut_reader_plugin/yaqut_reader_plugin_platform_interface.dart';
import 'package:yaqut_reader_plugin/yaqut_reader_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockYaqutReaderPluginPlatform
    with MockPlatformInterfaceMixin
    implements YaqutReaderPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final YaqutReaderPluginPlatform initialPlatform = YaqutReaderPluginPlatform.instance;

  test('$MethodChannelYaqutReaderPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelYaqutReaderPlugin>());
  });

  test('getPlatformVersion', () async {
    YaqutReaderPlugin yaqutReaderPlugin = YaqutReaderPlugin();
    MockYaqutReaderPluginPlatform fakePlatform = MockYaqutReaderPluginPlatform();
    YaqutReaderPluginPlatform.instance = fakePlatform;

    expect(await yaqutReaderPlugin.getPlatformVersion(), '42');
  });
}
