package android.src.main.java.co.reader.yaqut_reader_flutter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

import co.yaqut.reader.api.BookStorage;
import co.yaqut.reader.api.ReaderBuilder;
import co.yaqut.reader.api.ReaderStyle;
import co.yaqut.reader.api.SaveBookManager;

public class YaqutReaderPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private MethodChannel channel;
    private Context applicationContext;
    private Activity activity;
    private ReaderBuilder readerBuilder;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        applicationContext = flutterPluginBinding.getApplicationContext();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "yaqut_reader_plugin");
        channel.setMethodCallHandler(this);
        setAppearance();

        if (applicationContext instanceof Application) {
            // Initialize ReaderManager with Application context
            ReaderManager.initialize((Application) applicationContext);
        } else {
            throw new IllegalStateException("Unable to obtain Application instance from context");
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.d("YaqutReaderPlugin", "Method called: " + call.method);

        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "startReader":
                if (activity != null) {
                    Map<String, Object> arguments = call.arguments();
                    String header = (String) arguments.get("header");
                    String path = (String) arguments.get("path");
                    String token = (String) arguments.get("access_token");
                    Map<String, Object> book = (Map<String, Object>) arguments.get("book");
                    Map<String, Object> style = (Map<String, Object>) arguments.get("style");
                    startReader(header, path, token, book, style);
                } else {
                    result.error("NO_ACTIVITY", "Activity context is not available", null);
                }
                break;
            case "checkIfLocal":
                Map<String, Object> checkArgs = call.arguments();
                int bookId = (int) checkArgs.get("book_id");
                boolean isLocal = BookStorage.isBookLocal(applicationContext, bookId);
                result.success(isLocal);
                break;
            default:
                result.notImplemented();
        }
    }

    private void startReader(String header, String path, String token, Map<String, Object> bookData, Map<String, Object> styleData) {
        int bookId = (int) bookData.get("bookId");
        String title = (String) bookData.get("title");
        int bookFileId = (int) bookData.get("bookFileId");
        double previewPercentage = (Double) bookData.getOrDefault("samplePreviewPercentage", 0.15);
        int position = (int) bookData.getOrDefault("position", 0);

        int readerColor = (int) styleData.getOrDefault("readerColor", 0);
        int textSize = (int) styleData.getOrDefault("textSize", 22);
        boolean isJustified = (Boolean) styleData.getOrDefault("isJustified", true);
        int lineSpacing = (int) styleData.getOrDefault("lineSpacing", 1);
        int font = (int) styleData.getOrDefault("font", 0);

        ReaderStyle readerStyle = new ReaderStyle(textSize, readerColor, isJustified ? 1 : 0, lineSpacing, font);

        readerBuilder = new ReaderBuilder(activity, bookId); // Use Activity context here
        readerBuilder.setReaderStyle(readerStyle)
                .setTitle(title)
                .setPosition(position)
                .setPercentageView((float) previewPercentage);

        if (path.isEmpty()) {
            readerBuilder.build();
        } else {
            boolean saved = saveBook(bookId, path, header, token);
            if (saved) {
                readerBuilder.build();
            }
        }
    }

    private void setAppearance() {
        // Handle UI customization here
    }

    private boolean saveBook(int bookId, String bodyPath, String header, String accessToken) {
        return SaveBookManager.save(applicationContext, bookId, bodyPath, header, accessToken);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }
}
