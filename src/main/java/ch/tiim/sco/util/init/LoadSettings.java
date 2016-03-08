package ch.tiim.sco.util.init;

import ch.tiim.sco.config.Settings;
import javafx.concurrent.Task;

import java.nio.file.Paths;

public class LoadSettings extends Task<Settings> {
    @Override
    protected Settings call() throws Exception {
        return new Settings(Paths.get("settings.properties")); //NON-NLS
    }
}
