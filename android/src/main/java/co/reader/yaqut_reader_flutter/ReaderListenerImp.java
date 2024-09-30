package android.src.main.java.co.reader.yaqut_reader_flutter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.epubreader.ReaderConfiguration;
import com.example.epubreader.ReaderListener;
import com.example.epubreader.data.NotesAndMarks;

import java.util.ArrayList;
import java.util.List;

public class ReaderListenerImp implements ReaderListener, Parcelable {
    private  static MainActivity activity;

    public ReaderListenerImp(MainActivity activity) {
        // Default constructor
        ReaderListenerImp.activity = activity;
    }

    protected ReaderListenerImp(Parcel in) {
        // Read your data from the parcel
    }

    @Override
    public void onUpdateConfiguration(ReaderConfiguration readerConfiguration) {
        // Assuming MainActivity instance has a method to handle this
        activity.onConfigurationChanged(readerConfiguration);
    }

    @Override
    public void onUpdateBookPosition(int i) {
        activity.onPositionChanged(i);
    }

    @Override
    public void onUpdateLastOpened(long l) {
        // Implement your method here
    }

    @Override
    public void onSetLastModified(long l) {
        // Implement your method here
    }

    @Override
    public void onShareBook() {
        // Implement your method here
    }

    @Override
    public void onBookDetailsCLicked() {
        //open book details
    }

    @Override
    public void onSaveBookClicked() {
        // Implement your method here
    }

    @Override
    public void onDownloadBook() {
        // Implement your method here
    }

    @Override
    public void onSyncNotesAndMarks(List<NotesAndMarks> newNotes) {
            for (com.example.epubreader.data.NotesAndMarks newNote : newNotes)
                newNote.setSentToServer(false);
            List<NotesAndMarks> appNotesAndMarks = new ArrayList<>();
            for (int i = 0; i < newNotes.size(); i++) {
                appNotesAndMarks.add(new NotesAndMarks(newNotes.get(i).getFromOffset(), newNotes.get(i).getToOffset(), newNotes.get(i).getType(), newNotes.get(i).getDisplayText(), newNotes.get(i).getColor(), newNotes.get(i).isDeleted() ? 1 : 0));
            }
           activity.onSyncNotesAndMarks(appNotesAndMarks);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // Write your data to the parcel
    }

    public static final Creator<ReaderListenerImp> CREATOR = new Creator<ReaderListenerImp>() {
        @Override
        public ReaderListenerImp createFromParcel(Parcel in) {
            return new ReaderListenerImp(in);
        }

        @Override
        public ReaderListenerImp[] newArray(int size) {
            return new ReaderListenerImp[size];
        }
    };
}
