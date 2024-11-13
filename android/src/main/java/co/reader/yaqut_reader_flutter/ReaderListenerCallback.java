package co.reader.yaqut_reader_flutter;

import java.util.List;
import co.yaqut.reader.api.ReaderStyle;
import co.yaqut.reader.api.NotesAndMarks;

/**
 * Created by rula on 13,November,2024
 */
public interface ReaderListenerCallback {
    // Define an interface to notify the plugin
    void onStyleChanged(int bookId, ReaderStyle style);

    void onPositionChanged(int bookId, int position);

    void onSyncNotesAndMarks(int bookId, List<NotesAndMarks> notes);

    void onUpdateLastOpened(long i);

    void onShareBook();

    void onBookDetailsCLicked();

    void onSaveBookClicked(int i);

    void onDownloadBook();

    void onReaderClosed(int i);

    void onSampleEnded();

}
