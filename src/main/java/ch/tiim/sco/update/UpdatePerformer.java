package ch.tiim.sco.update;


import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.util.lang.ResourceBundleEx;
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
    private final ResourceBundleEx lang;

    public UpdatePerformer(EventBus eventBus, ResourceBundleEx lang) {

        this.eventBus = eventBus;
        this.lang = lang;
    }

    @Override
    protected Void call() throws Exception {
        try {
            if (VersionChecker.isNewUpdaterVersionAvailable()) {
                updateMessage(lang.str("task.update.installer"));
                updateUpdater();
            }
            updateMessage(lang.str("task.update.shutdown"));
            launchUpdater();
            System.exit(0);
        } catch (final IOException e) {
            ExceptionAlert.showError(LOGGER, lang.str("task.update.failed"), e);
        }
        return null;
    }

    private void updateUpdater() throws IOException {
        LOGGER.info("Downloading updater"); //NON-NLS
        Constants.downloadFile(Constants.REMOTE_UPDATER_URL, Constants.LOCAL_UPDATER_URL);
        Constants.downloadFile(
                Constants.REMOTE_UPDATER_VERSION_URL,
                Constants.LOCAL_UPDATER_VERSION_URL
        );
    }

    private void launchUpdater() throws IOException {
        LOGGER.info("Launch updater:"); //NON-NLS
        LOGGER.info(Constants.LAUNCH_UPDATER);
        Runtime.getRuntime().exec(Constants.LAUNCH_UPDATER);
    }
}
