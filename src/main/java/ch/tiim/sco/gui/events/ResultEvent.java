package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.ResultDialog;
import javafx.stage.Stage;

public class ResultEvent extends Event<Result> {
    private final Swimmer swimmer;

    public ResultEvent(Result obj, Swimmer swimmer) {
        super(obj);
        this.swimmer = swimmer;
    }

    public Swimmer getSwimmer() {
        return swimmer;
    }

    public static class ResultOpenEvent extends ResultEvent implements OpenEvent {

        private final Stage stage;

        public ResultOpenEvent(Result obj, Swimmer swimmer, Stage stage) {
            super(obj, swimmer);
            this.stage = stage;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return ResultDialog.class;
        }

        @Override
        public Stage getParent() {
            return stage;
        }
    }

    public static class ResultDeleteEvent extends ResultEvent {
        public ResultDeleteEvent(Result res, Swimmer swimmer) {
            super(res, swimmer);
        }
    }

    public static class ResultSaveEvent extends ResultEvent {
        public ResultSaveEvent(Result obj, Swimmer swimmer) {
            super(obj, swimmer);
        }
    }
}
