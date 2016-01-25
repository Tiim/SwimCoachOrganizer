package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.*;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.events.TrainingEvent;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TrainingView extends MainView {

    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private ListView<Training> trainings;
    @FXML
    private TableView<IndexedSet> selectedTraining;
    @FXML
    private TableColumn<IndexedSet, Number> colNr;
    @FXML
    private TableColumn<IndexedSet, String> colName;
    @FXML
    private TableColumn<IndexedSet, String> colDistance;
    @FXML
    private TableColumn<IndexedSet, String> colContent;
    @FXML
    private TableColumn<IndexedSet, Number> colIntensity;
    @FXML
    private TableColumn<IndexedSet, String> colFocus;
    @FXML
    private TableColumn<IndexedSet, String> colForm;
    @FXML
    private TableColumn<IndexedSet, String> colTime;

    @FXML
    private void initialize() {
        colNr.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getIndex()));
        colName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSet().getName()));
        colDistance.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getDistance()));
        colContent.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getContent()));
        colIntensity.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getSet().getIntensity()));
        colFocus.setCellValueFactory(d -> {
            SetFocus f = d.getValue().getSet().getFocus();
            return new SimpleStringProperty(f != null ? f.getAbbr() : "-");
        });
        colForm.setCellValueFactory(d -> {
            SetStroke f = d.getValue().getSet().getStroke();
            return new SimpleStringProperty(f != null ? f.getAbbr() : "-");
        });
        colTime.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getIntervalString()));
        trainings.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selected(newValue));
        selectedTraining.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) onDoubleClick();
        });

        populate();
    }

    private void selected(Training newValue) {
        if (newValue != null) {
            try {
                selectedTraining.getItems().setAll(db.getTblTrainingContent().getSets(newValue));
            } catch (Exception e) {
                LOGGER.warn("Can't load sets of " + newValue);
            }
        } else {
            selectedTraining.getItems().setAll();
        }
    }

    private void onDoubleClick() {
        Set set = selectedTraining.getSelectionModel().getSelectedItem().getSet();
        if (set != null) {
            eventBus.post(new SetEvent.SetInspectOpenEvent(set, mainStage));
        }
    }

    private void populate() {
        try {
            trainings.getItems().setAll(db.getTblTraining().getAllTrainings());
        } catch (Exception e) {
            LOGGER.warn("Can't load Trainings");
        }
    }

    @FXML
    private void onNew() {
        eventBus.post(new TrainingEvent.TrainingOpenEvent(null, mainStage));
    }

    @FXML
    private void onDelete() {
        Training item = trainings.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblTraining().deleteTraining(item);
            } catch (Exception e) {
                LOGGER.warn("Can't delete training", e);
            }
            eventBus.post(new TrainingEvent.TrainingDeleteEvent(item));
        }
    }

    @FXML
    private void onEdit() {
        Training item = trainings.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new TrainingEvent.TrainingOpenEvent(item, mainStage));
        }
    }

    @Subscribe
    public void onTraining(TrainingEvent event) {
        populate();
        trainings.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
