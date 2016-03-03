package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.events.FocusEvent;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.gui.util.Validator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FocusDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(FocusDialog.class);
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
        try {
            currentFocus = getFocus(currentFocus);
        } catch (UIException e) {
            e.showDialog("Missing Setting");
            return;
        }
        try {
            if (currentFocus.getId() == null) {
                db.getTblSetFocus().addSetFocus(currentFocus);
            } else {
                db.getTblSetFocus().updateSetFocus(currentFocus);
            }
        } catch (Exception e) {
            LOGGER.warn("Can't save stroke", e);
        }
        close();
        eventBus.post(new FocusEvent.FocusSaveEvent(currentFocus));
    }

    private SetFocus getFocus(SetFocus f) throws UIException {
        String name = Validator.strNotEmpty(this.name.getText(), "Name");
        String abbr = Validator.strNotEmpty(this.abbr.getText(), "Abbr");
        if (f == null) {
            f = new SetFocus(name, abbr, notes.getText());
        } else {
            f.setName(name);
            f.setAbbr(abbr);
            f.setNotes(notes.getText());
        }
        return f;
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}