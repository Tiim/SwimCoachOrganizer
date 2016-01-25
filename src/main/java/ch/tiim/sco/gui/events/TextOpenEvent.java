package ch.tiim.sco.gui.events;

import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.TextDialog;
import javafx.stage.Stage;

public class TextOpenEvent extends Event<String> implements OpenEvent {
    private final Stage parent;

    public TextOpenEvent(String obj, Stage parent) {
        super(obj);
        this.parent = parent;
    }

    @Override
    public Class<? extends DialogView> getDialog() {
        return TextDialog.class;
    }

    @Override
    public Stage getParent() {
        return parent;
    }
}
