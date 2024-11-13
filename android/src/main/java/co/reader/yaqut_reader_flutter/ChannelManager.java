package co.reader.yaqut_reader_flutter;

/**
 * Created by rula on 13,November,2024
 */
import io.flutter.plugin.common.MethodChannel;
public class ChannelManager {
    private static ChannelManager instance;
    private MethodChannel channel;

    private ChannelManager() {}

    public static synchronized ChannelManager getInstance() {
        if (instance == null) {
            instance = new ChannelManager();
        }
        return instance;
    }

    public void setChannel(MethodChannel channel) {
        this.channel = channel;
    }

    public MethodChannel getChannel() {
        return channel;
    }
}
