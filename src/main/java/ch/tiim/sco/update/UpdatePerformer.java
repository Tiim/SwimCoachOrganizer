package ch.tiim.sco.update;


import ch.tiim.sco.gui.alert.ExceptionAlert;
import com.google.common.eventbus.EventBus;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tim
 * @since 07 - 2014
 */
public class UpdatePerformer extends Task<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePerformer.class);
    private final EventBus eventBus;

    public UpdatePerformer(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @Override
    protected Void call() throws Exception {
        try {
            if (VersionChecker.isNewUpdaterVersionAvailable()) {
                updateMessage("Updating to new installer");
                LOGGER.info("Updating updater..");
                updateUpdater();
            }
            updateMessage("Launching installer, shutting down..");
            launchUpdater();
            System.exit(0);
        } catch (final IOException e) {
            ExceptionAlert.showError(LOGGER, "Something went wrong while updating", e);
        }
        return null;
    }

    private void updateUpdater() throws IOException {
        LOGGER.info("Downloading updater");
        Constants.downloadFile(Constants.REMOTE_UPDATER_URL, Constants.LOCAL_UPDATER_URL);
        Constants.downloadFile(
                Constants.REMOTE_UPDATER_VERSION_URL,
                Constants.LOCAL_UPDATER_VERSION_URL
        );
    }

    private void launchUpdater() throws IOException {
        LOGGER.info("Launch updater:");
        LOGGER.info(Constants.LAUNCH_UPDATER);
        Runtime.getRuntime().exec(Constants.LAUNCH_UPDATER);
    }
}
