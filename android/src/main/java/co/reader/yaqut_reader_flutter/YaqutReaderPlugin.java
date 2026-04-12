package co.reader.yaqut_reader_flutter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import co.yaqut.reader.api.BookInfo;
import co.yaqut.reader.api.FileSizeInfo;
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
import co.yaqut.reader.database.stats.DatabaseManager;

/**
 * Flutter plugin for Yaqut Reader integration.
 * Handles communication between Flutter and native Android reader library.
 */
public class YaqutReaderPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private MethodChannel channel;
    private Context applicationContext;
    private Activity activity;
    private ReaderBuilder readerBuilder;
    private static final String TAG = "YaqutReaderPlugin";
    private int bookId;

    // Flag to prevent double-open race condition
    private final AtomicBoolean isReaderOpening = new AtomicBoolean(false);
    private final AtomicBoolean isReaderOpen = new AtomicBoolean(false);

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        Log.i(TAG, "onAttachedToEngine: ");
        applicationContext = flutterPluginBinding.getApplicationContext();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "yaqut_reader_plugin");
        channel.setMethodCallHandler(this);
        ChannelManager.getInstance().setChannel(channel);

        if (applicationContext instanceof Application) {
            // Initialize ReaderManager with Application context
            ReaderManager.initialize((Application) applicationContext);
            // Initialize ReaderBuilder context for download progress broadcasts
            ReaderBuilder.initContext(applicationContext);
        } else {
            throw new IllegalStateException("Unable to obtain Application instance from context");
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        Log.i(TAG, "onDetachedFromEngine: ");
        channel.setMethodCallHandler(null);
        channel = null;
        ChannelManager.getInstance().setChannel(channel);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.d(TAG, "Method called: " + call.method);

        try {
            switch (call.method) {
                case "getPlatformVersion":
                    result.success("Android " + android.os.Build.VERSION.RELEASE);
                    break;

                case "startReader":
                    handleStartReader(call, result);
                    break;

                case "closeReader":
                    handleCloseReader(result);
                    break;

                case "checkIfLocal":
                    handleCheckIfLocal(call, result);
                    break;

                case "checkIfSample":
                    handleCheckIfSample(call, result);
                    break;

                case "getBookLength":
                    handleGetBookLength(call, result);
                    break;

                case "deleteSampleBook":
                    handleDeleteSampleBook(call, result);
                    break;

                case "getLocalBooks":
                    handleGetLocalBooks(result);
                    break;

                case "removeAllBooks":
                    handleRemoveAllBooks(result);
                    break;

                case "getLocalBooksInfo":
                    handleGetLocalBooksInfo(result);
                    break;

                case "hideReader":
                    ReaderBuilder.hideReader();
                    result.success(null);
                    break;

                case "showReader":
                    ReaderBuilder.showReader();
                    result.success(null);
                    break;

                case "updateMarks":
                    handleUpdateMarks(call, result);
                    break;

                case "updateDownloadProgress":
                    handleUpdateDownloadProgress(call, result);
                    break;

                case "offlineDownloadComplete":
                    handleOfflineDownloadComplete(call, result);
                    break;

                default:
                    result.notImplemented();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling method " + call.method + ": " + e.getMessage(), e);
            result.error("PLUGIN_ERROR", "Error in " + call.method + ": " + e.getMessage(), null);
        }
    }

    // MARK: - Download Progress Bridge (Flutter -> Native UI)

    private void handleUpdateDownloadProgress(MethodCall call, MethodChannel.Result result) {
        Map<String, Object> arguments = call.arguments();
        if (arguments == null) {
            Log.e(TAG, "DOWNLOAD TASK: ERROR - Arguments are null for updateDownloadProgress");
            result.error("INVALID_ARGUMENTS", "Arguments cannot be null", null);
            return;
        }

        Object bookIdObj = arguments.get("book_id");
        Object progressObj = arguments.get("progress");
        Object stateObj = arguments.get("state");

        if (!(bookIdObj instanceof Integer) || !(progressObj instanceof Double) || !(stateObj instanceof String)) {
            Log.e(TAG, "DOWNLOAD TASK: ERROR - Invalid arguments for updateDownloadProgress");
            result.error("INVALID_ARGUMENTS", "book_id, progress, and state are required", null);
            return;
        }

        int bookId = (Integer) bookIdObj;
        double progress = (Double) progressObj;
        String state = (String) stateObj;
        String error = (String) arguments.get("error");

        Log.d(TAG, "DOWNLOAD TASK: updateDownloadProgress() - bookId=" + bookId + ", state=" + state + ", progress=" + String.format("%.1f", progress * 100) + "%");

        // Update the native reader's progress UI via broadcast
        mainHandler.post(() -> {
            ReaderBuilder.updateDownloadProgress(bookId, (float) progress, state, 0, 0, error);
        });

        result.success(null);
    }

    // MARK: - Offline Download Complete (Delete key from database)

    private void handleOfflineDownloadComplete(MethodCall call, MethodChannel.Result result) {
        Map<String, Object> arguments = call.arguments();
        if (arguments == null) {
            Log.e(TAG, "DOWNLOAD TASK: ERROR - Arguments are null for offlineDownloadComplete");
            result.error("INVALID_ARGUMENTS", "Arguments cannot be null", null);
            return;
        }

        Object bookIdObj = arguments.get("book_id");
        if (!(bookIdObj instanceof Integer)) {
            Log.e(TAG, "DOWNLOAD TASK: ERROR - Invalid book_id for offlineDownloadComplete");
            result.error("INVALID_ARGUMENTS", "book_id must be an integer", null);
            return;
        }

        int bookId = (Integer) bookIdObj;
        Log.d(TAG, "DOWNLOAD TASK: offlineDownloadComplete() - Deleting key for bookId=" + bookId);

        try {
            // Delete the key from KEYS_TABLE since the book is now stored offline
            int rowsDeleted = DatabaseManager.getInstance(applicationContext).deleteKeyById(bookId);
            Log.d(TAG, "DOWNLOAD TASK: offlineDownloadComplete() - Deleted " + rowsDeleted + " key(s) for bookId=" + bookId);
            result.success(rowsDeleted > 0);
        } catch (Exception e) {
            Log.e(TAG, "DOWNLOAD TASK: ERROR - Failed to delete key for bookId=" + bookId + ": " + e.getMessage(), e);
            result.error("DELETE_KEY_ERROR", e.getMessage(), null);
        }
    }

    private void handleStartReader(MethodCall call, MethodChannel.Result result) {
        // Check for activity
        if (activity == null) {
            Log.e(TAG, "startReader: Activity is null");
            result.error("NO_ACTIVITY", "Activity context is not available. Reader cannot be started.", null);
            return;
        }

        // Prevent double-open race condition
        if (!isReaderOpening.compareAndSet(false, true)) {
            Log.w(TAG, "startReader: Reader is already being opened, ignoring duplicate request");
            result.error("READER_BUSY", "Reader is already being opened", null);
            return;
        }

        // If a reader is marked as open, close it first before opening a new one
        if (isReaderOpen.get()) {
            Log.i(TAG, "startReader: Previous reader was marked as open, closing it first");
            try {
                ReaderBuilder.closeReader();
            } catch (Exception e) {
                Log.w(TAG, "startReader: Error closing previous reader: " + e.getMessage());
            }
            isReaderOpen.set(false);
        }

        try {
            Map<String, Object> arguments = call.arguments();
            if (arguments == null) {
                result.error("INVALID_ARGUMENTS", "Arguments cannot be null", null);
                isReaderOpening.set(false);
                return;
            }

            String header = (String) arguments.get("header");
            String path = (String) arguments.get("path");
            String token = (String) arguments.get("access_token");
            String saved = (String) arguments.get("saved");
            Map<String, Object> book = (Map<String, Object>) arguments.get("book");
            Map<String, Object> style = (Map<String, Object>) arguments.get("style");
            boolean isDarkMode = getBooleanValue(arguments, "is_dark_mode", false);

            if (book == null) {
                result.error("INVALID_ARGUMENTS", "Book data cannot be null", null);
                isReaderOpening.set(false);
                return;
            }

            startReader(header, path, token, book, style, saved, isDarkMode);
            isReaderOpen.set(true);
            result.success(null);
        } catch (Exception e) {
            Log.e(TAG, "Error starting reader: " + e.getMessage(), e);
            result.error("START_READER_ERROR", e.getMessage(), null);
        } finally {
            isReaderOpening.set(false);
        }
    }

    private void handleCloseReader(MethodChannel.Result result) {
        try {
            ReaderBuilder.hideReader();
            isReaderOpen.set(false);
            result.success(null);
        } catch (Exception e) {
            Log.e(TAG, "Error closing reader: " + e.getMessage(), e);
            result.error("CLOSE_READER_ERROR", e.getMessage(), null);
        }
    }

    private void handleCheckIfLocal(MethodCall call, MethodChannel.Result result) {
        Map<String, Object> checkArgs = call.arguments();
        if (checkArgs == null || !checkArgs.containsKey("book_id")) {
            result.error("INVALID_ARGUMENTS", "book_id is required", null);
            return;
        }
        Object bookIdObj = checkArgs.get("book_id");
        if (!(bookIdObj instanceof Integer)) {
            result.error("INVALID_ARGUMENTS", "book_id must be an integer", null);
            return;
        }
        int bookId = (Integer) bookIdObj;
        boolean isLocal = BookStorage.isBookLocal(applicationContext, bookId);
        result.success(isLocal);
    }

    private void handleCheckIfSample(MethodCall call, MethodChannel.Result result) {
        if (!(call.arguments instanceof Map)) {
            result.error("INVALID_ARGUMENTS", "Arguments must be a map", null);
            return;
        }
        Map<String, Object> arguments = (Map<String, Object>) call.arguments;
        if (!arguments.containsKey("book_id") || !(arguments.get("book_id") instanceof Integer)) {
            result.error("INVALID_ARGUMENTS", "book_id must be an integer", null);
            return;
        }
        int bookId = (Integer) arguments.get("book_id");
        BookInfo bookInfo = BookStorage.getBookInfo(applicationContext, bookId);
        if (bookInfo == null) {
            result.success(false); // Book not found, treat as not sample
            return;
        }
        result.success(bookInfo.isSample());
    }

    private void handleGetBookLength(MethodCall call, MethodChannel.Result result) {
        if (!(call.arguments instanceof Map)) {
            result.success(0);
            return;
        }
        Map<String, Object> arguments = (Map<String, Object>) call.arguments;
        if (!arguments.containsKey("book_id") || !(arguments.get("book_id") instanceof Integer)) {
            result.success(0);
            return;
        }
        int bookId = (Integer) arguments.get("book_id");
        BookInfo bookInfo = BookStorage.getBookInfo(applicationContext, bookId);
        if (bookInfo == null) {
            Log.d(TAG, "getBookLength: bookId=" + bookId + ", bookInfo=null");
            result.success(0);
            return;
        }
        Log.d(TAG, "getBookLength: bookId=" + bookId + ", fileType=" + bookInfo.getFileType() + ", length=" + bookInfo.getLength() + ", pages=" + bookInfo.getPages() + ", isSample=" + bookInfo.isSample());
        if ("pdf".equals(bookInfo.getFileType())) {
            result.success(bookInfo.getPages());
        } else {
            result.success(bookInfo.getLength());
        }
    }

    private void handleDeleteSampleBook(MethodCall call, MethodChannel.Result result) {
        if (!(call.arguments instanceof Map)) {
            result.error("INVALID_ARGUMENTS", "Arguments must be a map", null);
            return;
        }
        Map<String, Object> arguments = (Map<String, Object>) call.arguments;
        if (!arguments.containsKey("book_id") || !(arguments.get("book_id") instanceof Integer)) {
            result.error("INVALID_ARGUMENTS", "book_id must be an integer", null);
            return;
        }
        int bookId = (Integer) arguments.get("book_id");
        try {
            BookStorage.deleteBook(applicationContext, bookId);
            result.success(true);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting book: " + e.getMessage(), e);
            result.error("DELETE_ERROR", e.getMessage(), null);
        }
    }

    private void handleGetLocalBooks(MethodChannel.Result result) {
        try {
            int[] localBooks = BookStorage.getLocalBooks(applicationContext);
            result.success(localBooks);
        } catch (Exception e) {
            Log.e(TAG, "Error getting local books: " + e.getMessage(), e);
            result.success(new int[0]); // Return empty array on error
        }
    }

    private void handleRemoveAllBooks(MethodChannel.Result result) {
        try {
            int[] localBooks = BookStorage.getLocalBooks(applicationContext);
            if (localBooks != null) {
                for (int bookId : localBooks) {
                    BookStorage.deleteBook(applicationContext, bookId);
                }
            }
            result.success(true);
        } catch (Exception e) {
            Log.e(TAG, "Error removing all books: " + e.getMessage(), e);
            result.error("REMOVE_ALL_ERROR", e.getMessage(), null);
        }
    }

    private void handleGetLocalBooksInfo(MethodChannel.Result result) {
        try {
            List<FileSizeInfo> filesInfo = BookStorage.getLocalBookFilesInfo(applicationContext);
            List<Map<String, Object>> serializedFilesInfo = new ArrayList<>();

            if (filesInfo != null) {
                for (FileSizeInfo fileInfo : filesInfo) {
                    if (fileInfo != null) {
                        Map<String, Object> fileData = new HashMap<>();
                        fileData.put("id", fileInfo.getId());
                        fileData.put("size", fileInfo.getFileSize());
                        serializedFilesInfo.add(fileData);
                    }
                }
            }
            result.success(serializedFilesInfo);
        } catch (Exception e) {
            Log.e(TAG, "Error getting local books info: " + e.getMessage(), e);
            result.success(new ArrayList<>()); // Return empty list on error
        }
    }

    private void handleUpdateMarks(MethodCall call, MethodChannel.Result result) {
        if (readerBuilder == null) {
            result.error("READER_NOT_INITIALIZED", "Reader has not been initialized", null);
            return;
        }
        if (!(call.arguments instanceof Map)) {
            result.error("INVALID_ARGUMENTS", "Arguments must be a map", null);
            return;
        }
        Map<String, Object> arguments = (Map<String, Object>) call.arguments;
        if (!arguments.containsKey("marks")) {
            result.error("INVALID_ARGUMENTS", "marks field is required", null);
            return;
        }
        try {
            List<Map<String, Object>> notesAndMarksData = (List<Map<String, Object>>) arguments.get("marks");
            List<NotesAndMarks> notesAndMarks = getNotesAndMarks(notesAndMarksData);
            readerBuilder.updateNotesAndMarks(notesAndMarks);
            result.success(null);
        } catch (Exception e) {
            Log.e(TAG, "Error updating marks: " + e.getMessage(), e);
            result.error("UPDATE_MARKS_ERROR", e.getMessage(), null);
        }
    }

    private void startReader(String header, String path, String token, Map<String, Object> bookData, Map<String, Object> styleData, String saved, boolean isDarkMode) {
        if (activity == null || channel == null) {
            Log.e(TAG, "Cannot start reader: Activity or Channel is null");
            ChannelManager.getInstance().sendError("READER_ERROR", "Activity or Channel is null", null);
            return;
        }

        // Extract book data with null safety
        bookId = getIntValue(bookData, "bookId", 0);
        String title = (String) bookData.get("title");
        int bookFileId = getIntValue(bookData, "bookFileId", 0);
        double previewPercentage = getDoubleValue(bookData, "previewPercentage", 0.15);
        int position = getIntValue(bookData, "position", 0);
        String cover = (String) bookData.get("coverThumbUrl");

        // Get notes and marks with null safety
        List<Map<String, Object>> notesAndMarksData = (List<Map<String, Object>>) bookData.get("notesAndMarks");
        List<NotesAndMarks> notesAndMarks = getNotesAndMarks(notesAndMarksData);

        // Handle Reader Style with null safety
        int readerColor = 0;
        int textSize = 22;
        boolean isJustified = true;
        int lineSpacing = 1;
        int font = 0;

        if (styleData != null) {
            readerColor = getIntValue(styleData, "readerColor", 0);
            textSize = getIntValue(styleData, "textSize", 22);
            isJustified = getBooleanValue(styleData, "isJustified", true);
            lineSpacing = getIntValue(styleData, "lineSpacing", 1);
            font = getIntValue(styleData, "font", 0);
        }

        ReaderStyle readerStyle = new ReaderStyle(textSize, readerColor, isJustified ? 1 : 0, lineSpacing, font);

        // Create reader listener that will mark reader as closed when closed callback is received
        ReaderListenerImpl readerListener = new ReaderListenerImpl(bookId) {
            @Override
            public void onReaderClosed(int position) {
                Log.d(TAG, "onReaderClosed: Resetting isReaderOpen flag");
                isReaderOpen.set(false);
                super.onReaderClosed(position);
            }

            @Override
            public void onBookForceEnd(int position) {
                Log.d(TAG, "onBookForceEnd: Resetting isReaderOpen flag");
                isReaderOpen.set(false);
                super.onBookForceEnd(position);
            }

            @Override
            public void onSampleEnded() {
                Log.d(TAG, "onSampleEnded: Resetting isReaderOpen flag");
                isReaderOpen.set(false);
                super.onSampleEnded();
            }
        };

        readerBuilder = new ReaderBuilder(activity, bookId);
        readerBuilder.setReaderStyle(readerStyle)
                .setTitle(title != null ? title : "")
                .setCover(cover != null ? cover : "")
                .setPosition(position)
                .setPercentageView((float) previewPercentage)
                .setReaderListener(readerListener)
                .setNotesAndMarks(notesAndMarks)
                .setReadingStatsListener(new StatsSessionListenerImpl())
                .setFileId(bookFileId)
                .setDarkMode(isDarkMode);

        // Set save state
        if ("true".equals(saved)) {
            readerBuilder.setSaveState(ReaderBuilder.SAVE_STATE_SAVED);
            readerBuilder.setDownloadEnabled(true);
        } else if ("false".equals(saved)) {
            readerBuilder.setSaveState(ReaderBuilder.SAVE_STATE_NOT_SAVED);
            readerBuilder.setDownloadEnabled(true);
        } else {
            readerBuilder.setSaveState(ReaderBuilder.SAVE_STATE_DISABLED);
            readerBuilder.setDownloadEnabled(false);
        }

        // Build reader
        if (path == null || path.isEmpty()) {
            readerBuilder.build();
        } else {
            boolean isSaved = saveBook(bookId, path, header, token);
            if (isSaved) {
                readerBuilder.build();
            } else {
                isReaderOpen.set(false);
                ChannelManager.getInstance().sendError("SAVE_BOOK_ERROR", "Failed to save book before opening", null);
            }
        }
    }

    /**
     * Safely get int value from map with default fallback
     */
    private int getIntValue(Map<String, Object> map, String key, int defaultValue) {
        if (map == null || !map.containsKey(key)) return defaultValue;
        Object value = map.get(key);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        return defaultValue;
    }

    /**
     * Safely get double value from map with default fallback
     */
    private double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        if (map == null || !map.containsKey(key)) return defaultValue;
        Object value = map.get(key);
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return defaultValue;
    }

    /**
     * Safely get boolean value from map with default fallback
     */
    private boolean getBooleanValue(Map<String, Object> map, String key, boolean defaultValue) {
        if (map == null || !map.containsKey(key)) return defaultValue;
        Object value = map.get(key);
        if (value instanceof Boolean) return (Boolean) value;
        return defaultValue;
    }

    /**
     * Convert Flutter note/mark data to native NotesAndMarks objects.
     * Returns empty list instead of null for null safety.
     */
    private static @NonNull ArrayList<NotesAndMarks> getNotesAndMarks(List<Map<String, Object>> notesAndMarksData) {
        if (notesAndMarksData == null || notesAndMarksData.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<NotesAndMarks> notesAndMarks = new ArrayList<>(notesAndMarksData.size());
        for (Map<String, Object> item : notesAndMarksData) {
            if (item == null) continue;

            int fromOffset = getIntValueStatic(item, "location", 0);
            int toOffset = getIntValueStatic(item, "length", 0);
            int markColor = getIntValueStatic(item, "color", 0);
            String displayText = (String) item.getOrDefault("note", "");
            int type = getIntValueStatic(item, "type", 0);
            int deleted = getIntValueStatic(item, "deleted", 0);

            NotesAndMarks noteAndMark = new NotesAndMarks(fromOffset, toOffset, type,
                    displayText != null ? displayText : "", markColor, deleted);
            notesAndMarks.add(noteAndMark);
        }
        return notesAndMarks;
    }

    /**
     * Static version for use in static context
     */
    private static int getIntValueStatic(Map<String, Object> map, String key, int defaultValue) {
        if (map == null || !map.containsKey(key)) return defaultValue;
        Object value = map.get(key);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        return defaultValue;
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
