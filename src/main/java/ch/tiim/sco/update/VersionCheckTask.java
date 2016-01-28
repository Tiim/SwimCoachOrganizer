package ch.tiim.sco.update;


import com.google.common.eventbus.EventBus;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VersionCheckTask extends Task<Void> {
    private static final Logger LOGGER = LogManager.getLogger(VersionCheckTask.class.getName());
    private final EventBus eventBus;

    public VersionCheckTask(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @Override
    protected Void call() throws Exception {
        updateProgress(25, 100);
        updateMessage("Version: " + VersionChecker.getCurrentVersion());
        updateProgress(50, 100);
        updateMessage("New Version" + VersionChecker.getRemoteVersion());
        LOGGER.info("Version " + VersionChecker.getCurrentVersion() + " --> " +
                VersionChecker.getRemoteVersion());
        updateProgress(75, 100);
        if (VersionChecker.isNewVersionAvailable()) {
            try {
                Thread.sleep(2000); //Wait a bit to not overwhelm the user.
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted");
            }
            updateProgress(100, 100);
            eventBus.post(new NewVersionEvent(
                    VersionChecker.getCurrentVersion(),
                    VersionChecker.getRemoteVersion()
            ));
        }
        return null;
    }
}
