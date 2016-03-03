package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.StrokeEvent;
import ch.tiim.sco.gui.util.UIException;
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
            e.showDialog("Missing Setting");
            return;
        }
        try {
            if (currentStroke.getId() == null) {
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

    private SetStroke getStroke(SetStroke f) throws UIException {
        String name = this.name.getText();
        String abbr = this.abbr.getText();
        if (name == null || name.isEmpty()) {
            throw new UIException("Name");
        }
        if (abbr == null || name.isEmpty()) {
            throw new UIException("Abbr");
        }
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