package co.reader.yaqut_reader_flutter;

import android.os.Parcel;
import android.os.Parcelable;
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
    private final MethodChannel channel;
    private  int bookId;

    // Constructor
    public ReaderListenerImpl(MethodChannel channel, int bookId) {
        // Initialize any fields here if needed
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
        // Read any data from the parcel here
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
        int lineSpace = style.getLineSpacing().getValue();
        int readerColor = style.getReaderColor();
        int fontIndex = style.getFont();
        int fontSize = style.getTextSize();
        int layout = style.isJustified() ? 1 : 2;
        int bookId = this.bookId;

        Map<String, Integer> data = new HashMap<>();
        data.put("line_space", lineSpace);
        data.put("reader_color", readerColor);
        data.put("font", fontIndex);
        data.put("font_size", fontSize);
        data.put("layout", layout);
        data.put("book_id", bookId);

        if (channel != null) {
            channel.invokeMethod("onStyleChanged", data);
        }
    }

    @Override
    public void onPositionChanged(int i) {
        Map<String, Integer> data = new HashMap<>();
        data.put("position", i);
        data.put("book_id", bookId );

        if (channel != null) {
            channel.invokeMethod("onPositionChanged", data);
        }
    }

    @Override
    public void onSyncNotesAndMarks(List<NotesAndMarks> list) {
        List<Map<String, Object>> items = new ArrayList<>();
        for (NotesAndMarks mark : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("book_id", bookId);
            item.put("mark_id", (mark.getMarkId() != null) ? mark.getMarkId() : 0);
            item.put("from_offset", mark.getFromOffset());
            item.put("to_offset", mark.getToOffset());
            item.put("mark_color", (mark.getMarkColor() != null) ? mark.getMarkColor() : 0);
            item.put("display_text", (mark.getDisplayText() != null) ? mark.getDisplayText() : "");
            item.put("type", mark.getType());
            item.put("deleted", (mark.getDeleted() != null) ? mark.getDeleted() : 0);
            item.put("local", (mark.getLocal() != null) ? mark.getLocal() : 1);

            items.add(item);
        }

        if (channel != null) {
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
            channel.invokeMethod("onShareBook", new HashMap<String, Object>());
        }
    }

    @Override
    public void onBookDetailsCLicked() {
        if (channel != null) {
            channel.invokeMethod("onBookDetailsCLicked", new HashMap<String, Object>());
        }

    }

    @Override
    public void onSaveBookClicked(int i) {
        Map<String, Integer> data = new HashMap<>();
        data.put("position", i);
        data.put("book_id", bookId);

        if (channel != null) {
            channel.invokeMethod("onSaveBookClicked", data);

        }
    }

    @Override
    public void onDownloadBook() {
        if (channel != null) {
            channel.invokeMethod("onDownloadBook", new HashMap<String, Object>());
        }
    }

    @Override
    public void onReaderClosed(int i) {
        Map<String, Integer> data = new HashMap<>();
        data.put("position", i);
        data.put("book_id", bookId );

        if (channel != null) {
            channel.invokeMethod("onReaderClosed", data);
        }
    }

    @Override
    public void onSampleEnded() {
        if (channel != null) {
            channel.invokeMethod("onSampleEnded", new HashMap<String, Object>());
        }
    }
}