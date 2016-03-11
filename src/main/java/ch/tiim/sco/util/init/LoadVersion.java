package ch.tiim.sco.util.init;

import ch.tiim.sco.update.VersionChecker;
import com.github.zafarkhaja.semver.Version;
import javafx.concurrent.Task;

public class LoadVersion extends Task<Version> {
    @Override
    protected Version call() throws Exception {
        updateProgress(0, 10);
        VersionChecker.getRemoteVersion();
        updateProgress(10, 10);
        return VersionChecker.getCurrentVersion();
    }
}
