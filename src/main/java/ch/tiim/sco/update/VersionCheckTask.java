package ch.tiim.sco.update;


import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VersionCheckTask implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(VersionCheckTask.class.getName());
    private final EventBus eventBus;

    public VersionCheckTask(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        LOGGER.info(String.format("Version %s --> %s",
                VersionChecker.getCurrentVersion(),
                VersionChecker.getRemoteVersion()));
        if (VersionChecker.isNewVersionAvailable()) {
            try {
                Thread.sleep(5000); //Wait a bit to not overwhelm the user.
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted");
            }
            eventBus.post(new NewVersionEvent(
                    VersionChecker.getCurrentVersion(),
                    VersionChecker.getRemoteVersion()
            ));
        }
    }
}
