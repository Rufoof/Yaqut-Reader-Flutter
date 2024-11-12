package co.reader.yaqut_reader_flutter;

import android.os.Parcel;
import android.os.Parcelable;
import io.flutter.plugin.common.MethodChannel;
import co.yaqut.reader.api.StatsSessionListener;
import co.yaqut.reader.api.ReadingSession;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class StatsSessionListenerImpl implements StatsSessionListener, Parcelable {
    private MethodChannel channel;

    // Constructor
    public StatsSessionListenerImpl(MethodChannel channel) {
        this.channel = channel;
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

    protected StatsSessionListenerImpl(Parcel in) {
        // Read any data from the parcel here
    }

    public static final Creator<StatsSessionListenerImpl> CREATOR = new Creator<StatsSessionListenerImpl>() {
        @Override
        public StatsSessionListenerImpl createFromParcel(Parcel in) {
            return new StatsSessionListenerImpl(in);
        }

        @Override
        public StatsSessionListenerImpl[] newArray(int size) {
            return new StatsSessionListenerImpl[size];
        }
    };

    // Implement the StatsSessionListener methods here
    @Override
    public void onReadingSessionEnd(ReadingSession session) {
        Map<String, Object> data = new HashMap<>();
        data.put("book_id", session.getBookId());
        data.put("book_file_id", session.getBookFileId());
        data.put("pages_read", session.getPagesRead());
        data.put("start_offset", session.getStartOffset());
        data.put("end_offset", session.getEndOffset());
        data.put("covered_offset", session.getCoveredOffset());
        data.put("covered_length", session.getCoveredLength());
        data.put("start_time", session.getStartTime());
        data.put("end_time", session.getEndTime());
        data.put("md5", session.getMd5());
        data.put("uuid", session.getUuid());

        if (channel != null) {
            channel.invokeMethod("onReadingSessionEnd", data);
        }

    }
}