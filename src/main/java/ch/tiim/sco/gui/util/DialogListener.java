package ch.tiim.sco.gui.util;

import ch.tiim.sco.gui.ViewLoader;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.TaskDialog;
import ch.tiim.sco.gui.events.OpenEvent;
import com.google.common.eventbus.Subscribe;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class DialogListener {

    private final ViewLoader viewLoader;

    public DialogListener(ViewLoader viewLoader){
        this.viewLoader = viewLoader;
    }

    @Subscribe
    public void onOpen(OpenEvent event) {
        DialogView dv = viewLoader.load(event.getDialog());
        dv.open(event, event.getParent());
    }

    @Subscribe
    public void onTask(Task<?> event) {
        TaskDialog dialog = viewLoader.load(TaskDialog.class);
        EventHandler<WorkerStateEvent> onSucceeded = event.getOnSucceeded();
        EventHandler<WorkerStateEvent> onFailed = event.getOnFailed();
        event.setOnSucceeded(event1 -> {
            if (onSucceeded != null) {
                onSucceeded.handle(event1);
            }
            dialog.onSucceeded();
        });
        event.setOnFailed(event1 -> {
            if (onFailed != null) {
                onFailed.handle(event1);
            }
            dialog.onFailed();
        });
        event.messageProperty().addListener((observable, oldValue, newValue) -> dialog.onMessageUpdate(newValue));
        event.progressProperty().addListener((observable, oldValue, newValue) ->
                dialog.onProgressUpdate(newValue.doubleValue()));
        dialog.open(null, null);
    }

}
