package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.util.ModelCell;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetView extends MainView {
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private ListView<Set> sets;

    @FXML
    private Label name;
    @FXML
    private Label intensity;
    @FXML
    private Label distance;
    @FXML
    private Label time;
    @FXML
    private Label focus;
    @FXML
    private Label stroke;
    @FXML
    private TextField content;
    @FXML
    private TextArea notes;

    @FXML
    private void initialize() {
        sets.setCellFactory(param -> new ModelCell<>());
        sets.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelected(newValue));
        populate();
    }

    private void onSelected(Set set) {
        if (set != null) {
            name.setText(set.getName());
            intensity.setText(String.valueOf(set.getIntensity()));
            distance.setText(set.getDistance());
            time.setText(set.getIntervalString());
            focus.setText(set.getFocus().uiString());
            stroke.setText(set.getStroke().uiString());
            content.setText(set.getContent());
            notes.setText(set.getNotes());
        }
    }

    private void populate() {
        try {
            sets.getItems().setAll(db.getTblSet().getAllSets());
        } catch (Exception e) {
            new ExceptionAlert(LOGGER, "Can't load sets.", e, eventBus).handle();
        }
    }

    @FXML
    private void onNew() {
        eventBus.post(new SetEvent.SetOpenEvent(null, mainStage));
    }

    @FXML
    private void onDelete() {
        Set set = sets.getSelectionModel().getSelectedItem();
        if (set != null) {
            try {
                db.getTblSet().deleteSet(set);
            } catch (Exception e) {
                new ExceptionAlert(LOGGER, "Can't delete set", e, eventBus).handle();
            }
            eventBus.post(new SetEvent.SetDeleteEvent(set));
        }
    }

    @FXML
    private void onEdit() {
        Set set = sets.getSelectionModel().getSelectedItem();
        if (set != null) {
            eventBus.post(new SetEvent.SetOpenEvent(set, mainStage));
        }
    }

    @Subscribe
    public void onSet(SetEvent event) {
        populate();
        sets.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
