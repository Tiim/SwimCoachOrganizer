package ch.tiim.sco.gui.dialog;

import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.TextOpenEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TextDialog extends DialogView {

    @FXML
    private BorderPane root;

    @FXML
    private TextArea text;

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((TextOpenEvent) event).getObj());
    }

    private void populate(String obj) {
        text.setText(obj);
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
