package ch.tiim.sco.gui.events;

import ch.tiim.sco.gui.dialog.DialogView;
import javafx.stage.Stage;

public interface OpenEvent {
    Class<? extends DialogView> getDialog();

    Stage getParent();
}
