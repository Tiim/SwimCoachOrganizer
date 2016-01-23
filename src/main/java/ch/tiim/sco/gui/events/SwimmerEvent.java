package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.SwimmerDialog;
import javafx.stage.Stage;

public abstract class SwimmerEvent extends Event<Swimmer> {

    public SwimmerEvent(Swimmer obj) {
        super(obj);
    }

    public static class SwimmerDeleteEvent extends SwimmerEvent {

        public SwimmerDeleteEvent(Swimmer obj) {
            super(obj);
        }
    }

    public static class SwimmerSaveEvent extends SwimmerEvent {

        public SwimmerSaveEvent(Swimmer obj) {
            super(obj);
        }
    }

    public static class SwimmerOpenEvent extends SwimmerEvent implements OpenEvent {

        private final Stage stage;

        public SwimmerOpenEvent(Swimmer obj, Stage stage) {
            super(obj);
            this.stage = stage;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return SwimmerDialog.class;
        }

        @Override
        public Stage getParent() {
            return stage;
        }
    }
}
