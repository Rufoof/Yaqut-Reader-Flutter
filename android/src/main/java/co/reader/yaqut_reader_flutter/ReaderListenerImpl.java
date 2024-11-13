package co.reader.yaqut_reader_flutter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import io.flutter.plugin.common.MethodChannel;
import co.yaqut.reader.api.ReaderListener;
import co.yaqut.reader.api.ReaderStyle;
import co.yaqut.reader.api.NotesAndMarks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class ReaderListenerImpl implements ReaderListener, Parcelable {
    private static final String TAG = "ReaderListenerImpl";
    private  MethodChannel channel;
    private  int bookId;

    // Constructor
    public ReaderListenerImpl(MethodChannel channel, int bookId) {
        // Initialize any fields here if needed
        Log.i(TAG, "ReaderListenerImpl: initialized channel = "+  channel);
        this.channel = channel;
        this.bookId = bookId;
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // Write any necessary data to the parcel here
    }

    protected ReaderListenerImpl(Parcel in) {

    }

    public static final Creator<ReaderListenerImpl> CREATOR = new Creator<ReaderListenerImpl>() {
        @Override
        public ReaderListenerImpl createFromParcel(Parcel in) {
            return new ReaderListenerImpl(in);
        }

        @Override
        public ReaderListenerImpl[] newArray(int size) {
            return new ReaderListenerImpl[size];
        }
    };

    // Implement the ReaderListener methods here
    @Override
    public void onStyleChanged(ReaderStyle style) {
        int lineSpace = style.getLineSpacing();
        int readerColor = style.getReaderColor();
        int fontIndex = style.getFont();
        int fontSize = style.getTextSize();
        int layout = style.isJustified();

        Map<String, Integer> data = new HashMap<>();
        data.put("line_space", lineSpace);
        data.put("reader_color", readerColor);
        data.put("font", fontIndex);
        data.put("font_size", fontSize);
        data.put("layout", layout);
        data.put("book_id", bookId);

        if (channel != null) {
            Log.i(TAG, "onStyleChanged: invokeMethod");
            channel.invokeMethod("onStyleChanged", data);
        }else
            Log.i(TAG, "onStyleChanged: channel is null");
    }

    @Override
    public void onPositionChanged(int i) {
        Map<String, Integer> data = new HashMap<>();
        data.put("position", i);
        data.put("book_id", bookId );

        if (channel != null) {
            Log.i(TAG, "onPositionChanged: invokeMethod");
            channel.invokeMethod("onPositionChanged", data);
        }
    }

    @Override
    public void onSyncNotesAndMarks(List<NotesAndMarks> list) {
        List<Map<String, Object>> items = new ArrayList<>();
        for (NotesAndMarks mark : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("book_id", bookId);
            item.put("from_offset", mark.getFromOffset());
            item.put("to_offset", mark.getToOffset());
            item.put("mark_color",  mark.getColor() );
            item.put("display_text", (mark.getDisplayText() != null) ? mark.getDisplayText() : "");
            item.put("type", mark.getType());
            item.put("deleted", mark.isDeleted() ? 1 : 0);

            items.add(item);
        }

        if (channel != null) {
            Log.i(TAG, "onSyncNotes: invokeMethod");
            channel.invokeMethod("onSyncNotes", items);
        }

    }

    @Override
    public void onUpdateLastOpened(long l) {
        // Handle updating last opened
    }

    @Override
    public void onShareBook() {
        if (channel != null) {
            Log.i(TAG, "onShareBook: invokeMethod");
            channel.invokeMethod("onShareBook", new HashMap<String, Object>());
        }

    }

    @Override
    public void onBookDetailsCLicked() {
        if (channel != null) {
            Log.i(TAG, "onBookDetailsCLicked: invokeMethod");
            channel.invokeMethod("onBookDetailsCLicked", new HashMap<String, Object>());
        }

    }

    @Override
    public void onSaveBookClicked(int i) {
        Map<String, Integer> data = new HashMap<>();
        data.put("position", i);
        data.put("book_id", bookId);

        if (channel != null) {
            Log.i(TAG, "onSaveBookClicked: invokeMethod");
            channel.invokeMethod("onSaveBookClicked", data);

        }
    }

    @Override
    public void onDownloadBook() {
        if (channel != null) {
            Log.i(TAG, "onDownloadBook: invokeMethod");
            channel.invokeMethod("onDownloadBook", new HashMap<String, Object>());
        }
    }

    @Override
    public void onReaderClosed(int i) {
        Map<String, Integer> data = new HashMap<>();
        data.put("position", i);
        data.put("book_id", bookId );

        if (channel != null) {
            Log.i(TAG, "onReaderClosed: invokeMethod");
            channel.invokeMethod("onReaderClosed", data);
        }
    }

    @Override
    public void onSampleEnded() {
        if (channel != null) {
            Log.i(TAG, "onSampleEnded: invokeMethod");
            channel.invokeMethod("onSampleEnded", new HashMap<String, Object>());
        }
    }
}