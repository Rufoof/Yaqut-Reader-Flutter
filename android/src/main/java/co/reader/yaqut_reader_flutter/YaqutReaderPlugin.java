package android.src.main.java.co.reader.yaqut_reader_flutter;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import com.example.yaqut_reader.ReaderBuilder;
import com.example.yaqut_reader.SaveBookManager;
import com.example.yaqut_reader.BookStorage;
import com.example.yaqut_reader.NotesAndMarks;
import com.example.yaqut_reader.ReaderStyle;
import com.example.yaqut_reader.ReaderDelegate;
import com.example.yaqut_reader.StatsSessionDelegate;

import java.util.List;
import java.util.Map;


/** YaqutReaderFlutterPlugin */
public class YaqutReaderPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

    private ReaderBuilder readerBuilder;
    private Integer bookId;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
      channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "yaqut_reader_plugin");
      channel.setMethodCallHandler(this);
      setAppearance();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
      channel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
      switch (call.method) {
        case "getPlatformVersion":
          result.success("Android " + android.os.Build.VERSION.RELEASE);
          break;
        case "saveBook":
          String header = (String) arguments.get("header");
          String path = (String) arguments.get("path");
          String token = (String) arguments.get("access_token");
          saveBook(path, header,token);
          break;
        case "startReader":
          Map<String, Object> arguments = call.arguments();
          Map<String, Object> book = (Map<String, Object>) arguments.get("book");
          Map<String, Object> style = (Map<String, Object>) arguments.get("style");
          startReader( book, style);
          break;
        case "checkIfLocal":
          Map<String, Object> checkArgs = call.arguments();
          int bookId = (Integer) checkArgs.get("book_id");
          int bookFileId = (Integer) checkArgs.get("book_file_id");
          BookStorage bookStorage = new BookStorage();
          boolean isLocal = bookStorage.isBookLocal(bookId);

          result.success(isLocal);
          break;
        default:
          result.notImplemented();
      }
    }

    private void startReader( Map<String, Object> bookData, Map<String, Object> styleData) {
      bookId = (Integer) bookData.get("bookId");
      String title = (String) bookData.get("title");
      int bookFileId = (Integer) ((Map<String, Object>) bookData.get("currentFile")).get("bookFileId");
      double previewPercentage = (Double) bookData.getOrDefault("samplePreviewPercentage", 0.15);
      int position = (Integer) bookData.get("position");

      // Handle Reader Style
      int readerColor = (Integer) styleData.getOrDefault("readerColor", 0);
      int textSize = (Integer) styleData.getOrDefault("textSize", 22);
      boolean isJustified = (Boolean) styleData.getOrDefault("isJustified", true);
      int lineSpacing = (Integer) styleData.getOrDefault("lineSpacing", 1);
      int font = (Integer) styleData.getOrDefault("font", 0);

      ReaderStyle readerStyle = new ReaderStyle(textSize, readerColor, isJustified, lineSpacing, font);

      readerBuilder = new ReaderBuilder(getContext(), mBook.getBookId());
      readerBuilder.setReaderStyle(readerStyle)
              .setTitle(title)
              .setCover(userBook.getCoverPath())
              .setPosition(position)
              .setPercentageView(previewPercentage)
              .setReaderListener(this)
              .setReadingStatsListener(this);
      readerBuilder.setFileId(bookFileId);
      readerBuilder.setNotesAndMarks(readerNotesAndMarks);
      readerBuilder.setSaveState(getSaveState()).setDownloadEnabled(getDownloadState());
      readerBuilder.build();
    }

    private void setAppearance() {
      // Handle UI customization here for Android equivalent
      // Colors and appearances can be customized via views and themes
    }
  private void saveBook(String bodyPath, String header, String accessToken) {
    SaveBookManager.save(getApplicationContext(), downloadRequest.getBookId(), bodyPath, header, accessToken);
  }
  }
