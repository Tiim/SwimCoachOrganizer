package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.StrokeEvent;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.gui.util.Validator;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrokeDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrokeDialog.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

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
    private void onCancel() {
        close();
    }

    @FXML
    private void onSave() {
        try {
            currentStroke = getStroke(currentStroke);
        } catch (UIException e) {
            e.showDialog(lang.str("gui.missing"));
            return;
        }
        try {
            if (currentStroke.getId() == null) {
                db.getTblSetStroke().addSetStroke(currentStroke);
            } else {
                db.getTblSetStroke().updateSetStroke(currentStroke);
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.save", "error.stroke"), e);
        }
        close();
        eventBus.post(new StrokeEvent.StrokeSaveEvent(currentStroke));
    }

    private SetStroke getStroke(SetStroke f) throws UIException {
        String name = Validator.strNotEmpty(this.name.getText(), lang.str("gui.name"));
        String abbr = Validator.strNotEmpty(this.abbr.getText(), lang.str("gui.abbr"));
        if (f == null) {
            f = new SetStroke(name, abbr, notes.getText());
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