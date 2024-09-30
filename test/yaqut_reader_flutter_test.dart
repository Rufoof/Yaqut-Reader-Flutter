import 'package:flutter_test/flutter_test.dart';
import 'package:yaqut_reader_flutter/yaqut_reader_flutter.dart';
import 'package:yaqut_reader_flutter/yaqut_reader_flutter_platform_interface.dart';
import 'package:yaqut_reader_flutter/yaqut_reader_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockYaqutReaderFlutterPlatform
    with MockPlatformInterfaceMixin
    implements YaqutReaderFlutterPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final YaqutReaderFlutterPlatform initialPlatform = YaqutReaderFlutterPlatform.instance;

  test('$MethodChannelYaqutReaderFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelYaqutReaderFlutter>());
  });

  test('getPlatformVersion', () async {
    YaqutReaderFlutter yaqutReaderFlutterPlugin = YaqutReaderFlutter();
    MockYaqutReaderFlutterPlatform fakePlatform = MockYaqutReaderFlutterPlatform();
    YaqutReaderFlutterPlatform.instance = fakePlatform;

    expect(await yaqutReaderFlutterPlugin.getPlatformVersion(), '42');
  });
}
