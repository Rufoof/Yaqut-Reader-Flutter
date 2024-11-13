package co.reader.yaqut_reader_flutter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import co.yaqut.reader.api.ReaderManager;
import co.yaqut.reader.api.NotesAndMarks;

public class YaqutReaderPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private MethodChannel channel;
    private Context applicationContext;
    private Activity activity;
    private ReaderBuilder readerBuilder;
    private static final String TAG = "YaqutReaderPlugin";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        Log.i(TAG, "onAttachedToEngine: ");
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
        Log.i(TAG, "onDetachedFromEngine: ");
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.i("YaqutReaderPlugin", "Method called: " + call.method);
        if (channel == null && applicationContext != null) {
            channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "yaqut_reader_plugin");
            Log.i(TAG, "onMethodCall: channel initialized");
        }
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
                    Log.e(TAG, "onMethodCall: NO_ACTIVITY Activity context is not available");
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
        if (activity == null || channel == null) {
            Log.e("YaqutReaderPlugin", "Cannot start reader: Activity or Channel is null");
            return;
        }
        int bookId = (int) bookData.get("bookId");
        String title = (String) bookData.get("title");
        int bookFileId = (int) bookData.get("bookFileId");
        double previewPercentage = (Double) bookData.getOrDefault("samplePreviewPercentage", 0.15);
        int position = (int) bookData.getOrDefault("position", 0);
        String cover = (String) bookData.get("coverThumbUrl");
        List<Map<String, Object>> notesAndMarksData = (List<Map<String, Object>>) bookData.getOrDefault("notesAndMarks", new ArrayList<>());
        List<NotesAndMarks> notesAndMarks = getNotesAndMarks(notesAndMarksData);
        // Handle Reader Style
        int readerColor = (int) styleData.getOrDefault("readerColor", 0);
        int textSize = (int) styleData.getOrDefault("textSize", 22);
        boolean isJustified = (Boolean) styleData.getOrDefault("isJustified", true);
        int lineSpacing = (int) styleData.getOrDefault("lineSpacing", 1);
        int font = (int) styleData.getOrDefault("font", 0);

        ReaderStyle readerStyle = new ReaderStyle(textSize, readerColor, isJustified ? 1 : 0, lineSpacing, font);

        readerBuilder = new ReaderBuilder(activity, bookId); // Use Activity context here
        readerBuilder.setReaderStyle(readerStyle)
                .setTitle(title)
                .setCover(cover)
                .setPosition(position)
                .setPercentageView((float) previewPercentage)
                .setReaderListener(new ReaderListenerImpl(channel, bookId))
                .setNotesAndMarks(notesAndMarks)
                .setReadingStatsListener(new StatsSessionListenerImpl(channel));
        readerBuilder.setFileId(bookFileId);
        readerBuilder.setNotesAndMarks(notesAndMarks);
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

    private static @NonNull List<NotesAndMarks> getNotesAndMarks(List<Map<String, Object>> notesAndMarksData) {
        if (notesAndMarksData == null)
            return null;
        List<NotesAndMarks> notesAndMarks = new ArrayList<>();
        for (Map<String, Object> item : notesAndMarksData) {
            Map<String, Object> newItem = new HashMap<>();
            int markId = (int) item.getOrDefault("id", 0);
            int fromOffset = (int) item.getOrDefault("location", 0);
            int toOffset = (int) item.getOrDefault("length", 0);
            int markColor = (int) item.getOrDefault("color", 0);
            String displayText = (String) item.getOrDefault("note", "");
            int type = (int) item.getOrDefault("type", 0);
            int deleted = (int) item.getOrDefault("deleted", 0);
            int local = 1;

            NotesAndMarks noteAndMark = new NotesAndMarks(fromOffset, toOffset, type, displayText, markColor, deleted);
            notesAndMarks.add(noteAndMark);
        }
        return notesAndMarks;
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
