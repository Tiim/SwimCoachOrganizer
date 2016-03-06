package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.gui.events.ErrorReportEvent;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.util.error.ErrorReport;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorReportingDialog extends DialogView {

    private static final String NON_WORD_CHARS = " .,-+\"*%&/()=?'[]{}<>\\_:;\n\r\t";

    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private TextArea data;
    @FXML
    private TextArea input;

    private ErrorReport report;

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((ErrorReportEvent.ErrorReportOpenEvent) event));
    }

    private void populate(ErrorReportEvent.ErrorReportOpenEvent event) {
        report = event.getObj();
        data.setText(report.getReport());
    }

    @FXML
    private void initialize() {
        data.setOnMouseClicked(event -> censorWordUnder(data.getCaretPosition()));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void censorWordUnder(int caretPosition) {
        double scrollTop = data.getScrollTop();
        int start = caretPosition;
        int end = caretPosition + 1;
        for (; start >= 0 && isWord(start - 1); start--) {
            //Nothing here
        }
        int length = data.getText().length();
        for (; end < length && isWord(end); end++) {
            //Nothing here
        }
        StringBuilder sb = new StringBuilder(data.getText());
        String asterisk = new String(new char[end - start]).replace("\0", "*");
        sb.replace(start, end, asterisk);
        data.setText(sb.toString());
        data.positionCaret(caretPosition);
        data.setScrollTop(scrollTop);
    }

    private boolean isWord(int position) {
        char c = data.getText().charAt(position);
        for (int i = 0; i < NON_WORD_CHARS.length(); i++) {
            if (c == NON_WORD_CHARS.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void onCancel() {
        close();
    }

    @FXML
    private void onReport() {
        report.setReport(data.getText() + "\n\n=== USER ===\n" + input.getText()); //NON-NLS
        new Thread(() -> {
            try {
                report.send();
            } catch (IOException e) {
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR,
                        lang.format("error.send", "error.subj.error_report") + ": "
                                + e.getMessage(), ButtonType.OK));
            }
        }).start();
        close();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
