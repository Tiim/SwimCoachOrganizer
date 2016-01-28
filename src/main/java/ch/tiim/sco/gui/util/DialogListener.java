package ch.tiim.sco.gui.util;

import ch.tiim.sco.gui.ViewLoader;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.TaskDialog;
import ch.tiim.sco.gui.events.OpenEvent;
import com.google.common.eventbus.Subscribe;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogListener {
    private static final Logger LOGGER = LogManager.getLogger(DialogListener.class.getName());

    @Subscribe
    public void onOpen(OpenEvent event) {
        DialogView dv = ViewLoader.load(event.getDialog());
        if (dv == null) {
            LOGGER.warn("Can't load fxml for " + event.getDialog());
        } else {
            dv.open(event, event.getParent());
        }
    }

    @Subscribe
    public void onTask(Task<?> event) {
        TaskDialog dialog = ViewLoader.load(TaskDialog.class);
        if (dialog != null) {
            event.setOnFailed(it -> dialog.onFailed());
            event.setOnSucceeded(it -> dialog.onSucceeded());
            event.messageProperty().addListener((observable, oldValue, newValue) -> dialog.onMessageUpdate(newValue));
            event.progressProperty().addListener((observable, oldValue, newValue) ->
                    dialog.onProgressUpdate(newValue.doubleValue()));
            dialog.open(null, null);
        } else {
            LOGGER.warn("Can't load fxml for TaskDialog");
        }
    }

}
