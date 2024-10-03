import android.os.Parcel;
import android.os.Parcelable;

public class ReaderListenerImpl implements ReaderListener, Parcelable {

    // Constructor
    public ReaderListenerImpl() {
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
    public void onStyleChanged(ReaderStyle readerStyle) {
        // Handle style changes
    }

    @Override
    public void onPositionChanged(int i) {
        // Handle position changes
    }

    @Override
    public void onSyncNotesAndMarks(List<NotesAndMarks> list) {
        // Handle sync notes and marks
    }

    @Override
    public void onUpdateLastOpened(long l) {
        // Handle updating last opened
    }

    @Override
    public void onShareBook() {
        // Handle book sharing
    }

    @Override
    public void onBookDetailsCLicked() {
        // Handle book details clicked
    }

    @Override
    public void onSaveBookClicked(int i) {
        // Handle save book clicked
    }

    @Override
    public void onDownloadBook() {
        // Handle download book
    }

    @Override
    public void onReaderClosed(int i) {
        // Handle reader closed
    }

    @Override
    public void onSampleEnded() {
        // Handle sample ended
    }
}