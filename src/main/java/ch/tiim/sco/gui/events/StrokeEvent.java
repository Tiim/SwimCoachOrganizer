package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.StrokeDialog;
import javafx.stage.Stage;

public abstract class StrokeEvent {

    private final SetStroke stroke;

    public StrokeEvent(SetStroke stroke) {
        this.stroke = stroke;
    }

    public SetStroke getStroke() {
        return stroke;
    }

    public static class StrokeDeleteEvent extends StrokeEvent {
        public StrokeDeleteEvent(SetStroke stroke) {
            super(stroke);
        }
    }

    public static class StrokeOpenEvent extends StrokeEvent implements OpenEvent {
        private final Stage parent;

        public StrokeOpenEvent(SetStroke stroke, Stage parent) {
            super(stroke);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return StrokeDialog.class;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }

    public static class StrokeSaveEvent extends StrokeEvent {
        public StrokeSaveEvent(SetStroke stroke) {
            super(stroke);
        }
    }
}
