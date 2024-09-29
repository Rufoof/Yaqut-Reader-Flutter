import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:yaqut_reader_plugin/yaqut_reader_plugin_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelYaqutReaderPlugin platform = MethodChannelYaqutReaderPlugin();
  const MethodChannel channel = MethodChannel('yaqut_reader_plugin');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
