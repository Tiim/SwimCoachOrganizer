package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.StrokeEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StrokeDialog extends DialogView {

    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private TextField name;
    @FXML
    private TextField abbr;
    @FXML
    private TextArea notes;

    private SetStroke currentStroke;

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((StrokeEvent.StrokeOpenEvent) event).getObj());
    }

    private void populate(SetStroke stroke) {
        currentStroke = stroke;
        if (stroke != null) {
            name.setText(stroke.getName());
            abbr.setText(stroke.getAbbr());
            notes.setText(stroke.getNotes());
        }
    }

    @FXML
    void onCancel() {
        close();
    }

    @FXML
    void onSave() {
        boolean isNew = false;
        if (currentStroke == null) {
            isNew = true;
            currentStroke = new SetStroke(name.getText(), abbr.getText(), notes.getText());
        } else {
            currentStroke.setName(name.getText());
            currentStroke.setAbbr(abbr.getText());
            currentStroke.setNotes(notes.getText());
        }
        try {
            if (isNew) {
                db.getTblSetStroke().addSetStroke(currentStroke);
            } else {
                db.getTblSetStroke().updateSetStroke(currentStroke);
            }
        } catch (Exception e) {
            LOGGER.warn("Can't save stroke");
        }
        close();
        eventBus.post(new StrokeEvent.StrokeSaveEvent(currentStroke));
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}