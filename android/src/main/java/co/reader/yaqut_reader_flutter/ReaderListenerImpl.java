package co.reader.yaqut_reader_flutter;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

import co.yaqut.reader.api.ReaderStyle;
import co.yaqut.reader.api.NotesAndMarks;
import co.yaqut.reader.api.ReaderListener;

public class ReaderListenerImpl implements ReaderListener, Parcelable {
    private final WeakReference<MethodChannel> weakChannel;
    private final int bookId;

    // Constructor
    public ReaderListenerImpl(MethodChannel channel, int bookId) {
        this.weakChannel = new WeakReference<>(channel);
        this.bookId = bookId;
    }

    // Getter for the channel
    private MethodChannel getChannel() {
        return weakChannel.get(); // Return channel if still available
    }

    // Parcelable implementation
    protected ReaderListenerImpl(Parcel in) {
        this.bookId = in.readInt();
        // If there is any other object to save, you can read them here
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        // If you added other fields, write them here
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

    @Override
    public int describeContents() {
        return 0; // No special contents
    }

    @Override
    public void onStyleChanged(ReaderStyle style) {
        MethodChannel channel = getChannel();
        if (channel != null) {
            Map<String, Integer> data = new HashMap<>();
            data.put("line_space", style.getLineSpacing());
            data.put("reader_color", style.getReaderColor());
            data.put("font", style.getFont());
            data.put("font_size", style.getTextSize());
            data.put("layout", style.isJustified());
            data.put("book_id", bookId);
            channel.invokeMethod("onStyleChanged", data);
        }
    }

    @Override
    public void onPositionChanged(int position) {
        MethodChannel channel = getChannel();
        if (channel != null) {
            Map<String, Integer> data = new HashMap<>();
            data.put("position", position);
            data.put("book_id", bookId);
            channel.invokeMethod("onPositionChanged", data);
        }
    }

    @Override
    public void onSyncNotesAndMarks(List<NotesAndMarks> list) {
        MethodChannel channel = getChannel();
        if (channel != null) {
            List<Map<String, Object>> items = new ArrayList<>();
            for (NotesAndMarks mark : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("book_id", bookId);
                item.put("from_offset", mark.getFromOffset());
                item.put("to_offset", mark.getToOffset());
                item.put("mark_color", mark.getColor());
                item.put("display_text", mark.getDisplayText() != null ? mark.getDisplayText() : "");
                item.put("type", mark.getType());
                item.put("deleted", mark.isDeleted() ? 1 : 0);
                items.add(item);
            }
            channel.invokeMethod("onSyncNotes", items);
        }
    }

    @Override
    public void onUpdateLastOpened(long timestamp) {
        MethodChannel channel = getChannel();
        if (channel != null) {
            channel.invokeMethod("onUpdateLastOpened", timestamp);
        }
    }

    @Override
    public void onShareBook() {
        MethodChannel channel = getChannel();
        if (channel != null) {
            channel.invokeMethod("onShareBook", new HashMap<String, Object>());
        }
    }

    @Override
    public void onBookDetailsCLicked() {
        MethodChannel channel = getChannel();
        if (channel != null) {
            channel.invokeMethod("onBookDetailsClicked", new HashMap<String, Object>());
        }
    }

    @Override
    public void onSaveBookClicked(int position) {
        MethodChannel channel = getChannel();
        if (channel != null) {
            Map<String, Integer> data = new HashMap<>();
            data.put("position", position);
            data.put("book_id", bookId);
            channel.invokeMethod("onSaveBookClicked", data);
        }
    }

    @Override
    public void onDownloadBook() {
        MethodChannel channel = getChannel();
        if (channel != null) {
            channel.invokeMethod("onDownloadBook", new HashMap<String, Object>());
        }
    }

    @Override
    public void onReaderClosed(int position) {
        MethodChannel channel = getChannel();
        if (channel != null) {
            Map<String, Integer> data = new HashMap<>();
            data.put("position", position);
            data.put("book_id", bookId);
            channel.invokeMethod("onReaderClosed", data);
        }
    }

    @Override
    public void onSampleEnded() {
        MethodChannel channel = getChannel();
        if (channel != null) {
            channel.invokeMethod("onSampleEnded", new HashMap<String, Object>());
        }
    }
}
