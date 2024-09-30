package co.reader.yaqut_reader_plugin_example

import io.flutter.embedding.android.FlutterActivity

class MainActivity: FlutterActivity() {
    @Override
public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    MethodChannel channel = new MethodChannel(binding.getBinaryMessenger(), "yaqut_reader_plugin");
    channel.setMethodCallHandler(new YaqutReaderPlugin());
}
}
