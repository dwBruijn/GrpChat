package Client;

import Client.Utils.*;

public class MainConfiguration {

    private static final StageConfiguration stageConfiguration = StageConfiguration.getInstance();
    private static final StreamConfiguration streamConfiguration = StreamConfiguration.getInstance();
    private static final MessageConfiguration messageConfiguration = MessageConfiguration.getInstance();

    public static StageConfiguration getStageConfiguration() {
        return stageConfiguration;
    }

    public static StreamConfiguration getStreamConfiguration() {
        return streamConfiguration;
    }

    public static MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }
}
