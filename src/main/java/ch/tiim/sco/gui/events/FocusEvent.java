package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.dialog.DialogView;
import javafx.stage.Stage;

public abstract class FocusEvent {

    private final SetFocus focus;


    public FocusEvent(SetFocus focus) {
        this.focus = focus;
    }

    public SetFocus getFocus() {
        return focus;
    }

    public static class FocusSaveEvent extends FocusEvent {
        public FocusSaveEvent(SetFocus focus) {
            super(focus);
        }
    }

    public static class FocusDeleteEvent extends FocusEvent {
        public FocusDeleteEvent(SetFocus focus) {
            super(focus);
        }
    }

    public static class FocusOpenEvent extends FocusEvent implements OpenEvent {

        private final Stage parent;

        public FocusOpenEvent(SetFocus focus, Stage parent) {
            super(focus);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return null;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }
}
