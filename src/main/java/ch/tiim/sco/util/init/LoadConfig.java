package ch.tiim.sco.util.init;

import ch.tiim.sco.config.Config;
import javafx.concurrent.Task;

public class LoadConfig extends Task<Config> {
    @Override
    protected Config call() throws Exception {
        return new Config();
    }
}
