package ch.tiim.sco.update;


import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionCheckTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckTask.class);
    private final EventBus eventBus;

    public VersionCheckTask(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        LOGGER.info(String.format("Version %s --> %s", //NON-NLS
                VersionChecker.getCurrentVersion(),
                VersionChecker.getRemoteVersion()));
        if (VersionChecker.isNewVersionAvailable()) {
            try {
                Thread.sleep(5000); //Wait a bit to not overwhelm the user.
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted"); //NON-NLS
            }
            eventBus.post(new NewVersionEvent(
                    VersionChecker.getCurrentVersion(),
                    VersionChecker.getRemoteVersion()
            ));
        }
    }
}
