package co.reader.yaqut_reader_flutter;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;

import co.yaqut.reader.api.ReaderListener;
import co.yaqut.reader.api.ReadingSession;
import co.yaqut.reader.api.StatsSessionListener;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.util.Log;
import android.view.View;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry;
import co.yaqut.reader.api.ReaderBuilder;
import co.yaqut.reader.api.SaveBookManager;
import co.yaqut.reader.api.BookStorage;
import co.yaqut.reader.api.NotesAndMarks;
import co.yaqut.reader.api.ReaderStyle;
import co.yaqut.reader.api.ReaderManager;

import java.util.List;
import java.util.Map;

import android.os.Parcelable;

import co.reader.yaqut_reader_flutter.ReaderListenerImpl;
import co.reader.yaqut_reader_flutter.StatsSessionListenerImpl;

import android.app.Application;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodChannel;

/**
 * YaqutReaderFlutterPlugin
 */
public class YaqutReaderPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
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
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    // Method to start the reader, ensuring the Activity context is used
    private void startReader() {
        if (activity != null) {
            ReaderBuilder builder = new ReaderBuilder();
            builder.initMiniPlayer(activity); // Pass the Activity context here
            builder.build();
        } else {
            throw new IllegalStateException("Activity context is required to start the reader.");
        }
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

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.d("YaqutReaderPlugin", "Method called: " + call.method);
        if (call.method.equals("checkIfLocal")) {
            Log.d("YaqutReaderPlugin", "checkIfLocal invoked");
            Map<String, Object> checkArgs = call.arguments();
            int bookId = (int) checkArgs.get("book_id");
            int bookFileId = (int) checkArgs.get("book_file_id");
            boolean isLocal = BookStorage.isBookLocal(applicationContext, bookId);
            result.success(isLocal);
            return;
        }
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "startReader":
                Map<String, Object> arguments = call.arguments();
                String header = (String) arguments.get("header");
                String path = (String) arguments.get("path");
                String token = (String) arguments.get("access_token");
                Map<String, Object> book = (Map<String, Object>) arguments.get("book");
                Map<String, Object> style = (Map<String, Object>) arguments.get("style");
                startReader(header, path, token, book, style);
                break;
            case "checkIfLocal":
                Map<String, Object> checkArgs = call.arguments();
                int bookId = (int) checkArgs.get("book_id");
                int bookFileId = (int) checkArgs.get("book_file_id");
                boolean isLocal = BookStorage.isBookLocal(applicationContext,bookId);

                result.success(isLocal);
                break;
            default:
                result.notImplemented();
        }
    }

    private void startReader(String header, String path, String token,Map<String, Object> bookData, Map<String, Object> styleData) {
        int bookId = (int) bookData.get("bookId"); 

        String title = (String) bookData.get("title");
        int bookFileId = (int) bookData.get("bookFileId"); 
        double previewPercentage = (Double) bookData.getOrDefault("samplePreviewPercentage", 0.15);
        int position = (int) bookData.getOrDefault("position", 0);

        // Handle Reader Style
        int readerColor = (int) styleData.getOrDefault("readerColor", 0);
        int textSize = (int) styleData.getOrDefault("textSize", 22);
        boolean isJustified = (Boolean) styleData.getOrDefault("isJustified", true);
        int lineSpacing = (int) styleData.getOrDefault("lineSpacing", 1);
        int font = (int) styleData.getOrDefault("font", 0);

        ReaderStyle readerStyle = new ReaderStyle(textSize, readerColor, isJustified ? 1 : 0, lineSpacing, font);

        readerBuilder = new ReaderBuilder(applicationContext, bookId);
        readerBuilder.setReaderStyle(readerStyle)
                .setTitle(title)
                .setCover("")
                .setPosition(position)
                .setPercentageView((float) previewPercentage)
                .setReaderListener(readerListener)
                .setReadingStatsListener(statListener);
        readerBuilder.setFileId(bookFileId);
        readerBuilder.setNotesAndMarks(null);
        readerBuilder.setSaveState(ReaderBuilder.SAVE_STATE_NOT_SAVED).setDownloadEnabled(false);

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
        // Handle UI customization here for Android equivalent
        // Colors and appearances can be customized via views and themes
    }

    private boolean saveBook(int bookId, String bodyPath, String header, String accessToken) {
        return SaveBookManager.save(applicationContext, bookId, bodyPath, header, accessToken);
    }
    private ReaderListenerImpl readerListener = new ReaderListenerImpl();
    private StatsSessionListenerImpl statListener = new StatsSessionListenerImpl();
}