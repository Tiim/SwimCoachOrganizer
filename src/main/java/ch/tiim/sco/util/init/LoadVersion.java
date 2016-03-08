package ch.tiim.sco.util.init;

import ch.tiim.sco.update.VersionChecker;
import com.github.zafarkhaja.semver.Version;
import javafx.concurrent.Task;

public class LoadVersion extends Task<Version> {
    @Override
    protected Version call() throws Exception {
        VersionChecker.getRemoteVersion();
        return VersionChecker.getCurrentVersion();
    }
}
