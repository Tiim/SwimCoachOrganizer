package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class TaskDialog extends DialogView {

    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private BorderPane root;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private TextArea message;


    public void onMessageUpdate(String s) {
        if (s != null) {
            message.appendText(s + "\n");
        }
    }

    public void onProgressUpdate(double i) {
        progress.setProgress(i);
    }

    public void onFailed() {
        progress.setProgress(-1);
        progress.setDisable(true);
        message.appendText(lang.str("gui.failed"));
        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(event -> close());
        pt.play();
    }

    public void onSucceeded() {
        close();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
