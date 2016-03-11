package ch.tiim.sco.util.init;

import ch.tiim.sco.config.Settings;
import javafx.concurrent.Task;

import java.nio.file.Paths;

public class LoadSettings extends Task<Settings> {
    @Override
    protected Settings call() throws Exception {
        updateProgress(0, 10);
        Settings s = new Settings(Paths.get("settings.properties")); //NON-NLS
        updateProgress(10, 10);
        return s;
    }
}
