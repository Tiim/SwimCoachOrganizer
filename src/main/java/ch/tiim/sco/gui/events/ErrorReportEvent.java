package ch.tiim.sco.gui.events;


import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.ErrorReportingDialog;
import ch.tiim.sco.util.error.ErrorReport;
import javafx.stage.Stage;

public class ErrorReportEvent extends Event<ErrorReport> {

    public ErrorReportEvent(ErrorReport obj) {
        super(obj);
    }

    public static class ErrorReportOpenEvent extends ErrorReportEvent implements OpenEvent {

        private final Stage parent;

        public ErrorReportOpenEvent(ErrorReport obj, Stage parent) {
            super(obj);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return ErrorReportingDialog.class;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }
}
