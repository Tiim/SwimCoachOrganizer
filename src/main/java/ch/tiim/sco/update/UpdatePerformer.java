package ch.tiim.sco.update;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Tim
 * @since 07 - 2014
 */
public class UpdatePerformer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(UpdatePerformer.class.getName());

    @Override
    public void run() {
        try {
            if (VersionChecker.isNewUpdaterVersionAvailable()) {
                LOGGER.info("Updating updater..");
                updateUpdater();
            }
            launchUpdater();
        } catch (final IOException e) {
            LOGGER.warn("", e);
        }
        System.exit(0);
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
