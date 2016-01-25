package ch.tiim.sco.gui.events;

import ch.tiim.sco.gui.dialog.AboutDialog;
import ch.tiim.sco.gui.dialog.DialogView;
import javafx.stage.Stage;

public class AboutEvent implements OpenEvent {
    private final Stage parent;

    public AboutEvent(Stage parent) {

        this.parent = parent;
    }

    @Override
    public Class<? extends DialogView> getDialog() {
        return AboutDialog.class;
    }

    @Override
    public Stage getParent() {
        return parent;
    }
}
