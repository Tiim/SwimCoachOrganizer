package ch.tiim.sco.gui.dialog;

import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.SetEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetInspectDialog extends DialogView {

    @FXML
    private Parent root;
    @FXML
    private Label name;
    @FXML
    private TextField content;
    @FXML
    private TextArea notes;
    @FXML
    private Label distance;
    @FXML
    private Label intensity;
    @FXML
    private Label time;
    @FXML
    private Label stroke;
    @FXML
    private Label focus;

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((SetEvent.SetInspectOpenEvent) event).getObj());
    }

    private void populate(Set set) {
        name.setText(set.getName());
        content.setText(set.getContent());
        notes.setText(set.getNotes());
        distance.setText(set.getDistance());
        intensity.setText(set.getIntensity() + "%");
        time.setText(set.getIntervalString());
        stroke.setText(set.getStroke().uiString());
        focus.setText(set.getFocus().uiString());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
