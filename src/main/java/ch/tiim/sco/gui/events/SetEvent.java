package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.SetDialog;
import ch.tiim.sco.gui.dialog.SetInspectDialog;
import javafx.stage.Stage;

public abstract class SetEvent {

    private final Set set;

    public SetEvent(Set set) {
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

    public static class SetDeleteEvent extends SetEvent {

        public SetDeleteEvent(Set set) {
            super(set);
        }
    }

    public static class SetOpenEvent extends SetEvent implements OpenEvent {
        private final Stage parent;

        public SetOpenEvent(Set set, Stage parent) {
            super(set);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return SetDialog.class;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }

    public static class SetInspectOpenEvent extends SetEvent implements OpenEvent {
        private final Stage parent;

        public SetInspectOpenEvent(Set set, Stage parent) {
            super(set);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return SetInspectDialog.class;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }

    public static class SetSaveEvent extends SetEvent {

        public SetSaveEvent(Set set) {
            super(set);
        }
    }
}
