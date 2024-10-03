import android.os.Parcel;
import android.os.Parcelable;

public class StatsSessionListenerImpl implements StatsSessionListener, Parcelable {

    // Constructor
    public StatsSessionListenerImpl() {
        // Initialize any fields here if needed
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
    public void onReadingSessionEnd(ReadingSession readingSession) {
        // Handle reading session end
    }
}