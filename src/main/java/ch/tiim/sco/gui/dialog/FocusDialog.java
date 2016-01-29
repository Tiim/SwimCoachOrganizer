package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.events.FocusEvent;
import ch.tiim.sco.gui.events.OpenEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FocusDialog extends DialogView {

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

    private SetFocus currentFocus;

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((FocusEvent) event).getObj());
    }

    private void populate(SetFocus stroke) {
        currentFocus = stroke;
        if (stroke != null) {
            name.setText(stroke.getName());
            abbr.setText(stroke.getAbbr());
            notes.setText(stroke.getNotes());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    @FXML
    private void onSave() {
        boolean isNew = false;
        if (currentFocus == null) {
            isNew = true;
            currentFocus = new SetFocus(name.getText(), abbr.getText(), notes.getText());
        } else {
            currentFocus.setName(name.getText());
            currentFocus.setAbbr(abbr.getText());
            currentFocus.setNotes(notes.getText());
        }
        try {
            if (isNew) {
                db.getTblSetFocus().addSetFocus(currentFocus);
            } else {
                db.getTblSetFocus().updateSetFocus(currentFocus);
            }
        } catch (Exception e) {
            LOGGER.warn("Can't save stroke");
        }
        close();
        eventBus.post(new FocusEvent.FocusSaveEvent(currentFocus));
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}