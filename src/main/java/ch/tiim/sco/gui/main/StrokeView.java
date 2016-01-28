package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.StrokeEvent;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class StrokeView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;

    @FXML
    private Parent root;
    @FXML
    private ListView<SetStroke> strokes;
    @FXML
    private Label name;
    @FXML
    private Label abbr;
    @FXML
    private TextArea notes;

    @FXML
    private void initialize() {
        strokes.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelect(newValue));
        populate();
    }

    private void onSelect(SetStroke stroke) {
        if (stroke != null) {
            name.setText(stroke.getName());
            abbr.setText(stroke.getAbbr());
            notes.setText(stroke.getNotes());
        }
    }

    private void populate() {
        try {
            strokes.setItems(FXCollections.observableArrayList(db.getTblSetStroke().getAllStrokes()));
        } catch (Exception e) {
            new ExceptionAlert(LOGGER, "Can't load strokes", e, eventBus).handle();
        }
    }

    @FXML
    private void onNew() {
        eventBus.post(new StrokeEvent.StrokeOpenEvent(null, mainStage));
    }

    @FXML
    private void onDelete() {
        SetStroke item = strokes.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblSetStroke().deleteSetStroke(item);
            } catch (Exception e) {
                new ExceptionAlert(LOGGER, "Can't delete focus", e, eventBus).handle();
            }
            eventBus.post(new StrokeEvent.StrokeDeleteEvent(item));
        }
    }

    @FXML
    private void onEdit() {
        SetStroke item = strokes.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new StrokeEvent.StrokeOpenEvent(item, mainStage));
        }
    }

    @Subscribe
    public void onStroke(StrokeEvent event) {
        populate();
        strokes.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
