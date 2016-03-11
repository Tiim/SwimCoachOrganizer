package ch.tiim.sco.util.init;

import ch.tiim.sco.config.Config;
import javafx.concurrent.Task;

public class LoadConfig extends Task<Config> {
    @Override
    protected Config call() throws Exception {
        updateProgress(0, 10);
        Config config = new Config();
        updateProgress(10, 10);
        return config;
    }
}
