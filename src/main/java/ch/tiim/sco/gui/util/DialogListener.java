package ch.tiim.sco.gui.util;

import ch.tiim.sco.gui.ViewLoader;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.events.OpenEvent;
import com.google.common.eventbus.Subscribe;
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

}
