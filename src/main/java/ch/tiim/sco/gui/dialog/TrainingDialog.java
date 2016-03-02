package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.*;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.events.TrainingEvent;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.gui.util.ModelConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TrainingDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDialog.class);
    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox<Team> teams;
    @FXML
    private ChoiceBox<ScheduleRule> schedules;
    @FXML
    private TableView<IndexedSet> training;
    @FXML
    private TableColumn<IndexedSet, Number> colNr;
    @FXML
    private TableColumn<IndexedSet, String> colName;
    @FXML
    private TableColumn<IndexedSet, String> colContent;

    @FXML
    private ListView<Set> sets;

    private Training currentTraining;

    @FXML
    private void initialize() {
        sets.setCellFactory(param -> new ModelCell<>());
        teams.setConverter(new ModelConverter<>());
        schedules.setConverter(new ModelConverter<>());

        colNr.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIndex()));
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getName()));
        colContent.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getContent()));

        date.valueProperty().addListener(observable -> populateSchedules());

        try {
            teams.getItems().setAll(db.getTblTeam().getAllTeams());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load teams", e, eventBus);
        }
    }

    private void populateSchedules() {
        try {
            List<ScheduleRule> t = db.getProcSchedule().getTrainingsForDay(date.getValue());
            if (teams.getValue() != null) {
                t = t.stream()
                        .filter(it -> it.getTeam().equals(teams.getValue()))
                        .collect(Collectors.toList());
            }
            schedules.getItems().setAll(t);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load schedules", e, eventBus);
        }
    }

    @FXML
    private void onRemove() {
        int index = training.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            training.getItems().remove(index);
        }
    }

    @FXML
    private void onAdd() {
        Set set = sets.getSelectionModel().getSelectedItem();
        if (set != null) {
            training.getItems().add(new IndexedSet(training.getItems().size() + 1, set));
        }
    }

    @FXML
    private void onInspect() {
        Set set = null;
        if (sets.isFocused()) {
            set = sets.getSelectionModel().getSelectedItem();
        } else if (training.isFocused()) {
            set = training.getSelectionModel().getSelectedItem().getSet();
        }
        if (set != null) {
            eventBus.post(new SetEvent.SetInspectOpenEvent(set, getStage()));
        }
    }

    @FXML
    private void onUp() {
        int idx = training.getSelectionModel().getSelectedIndex();
        if (idx > 0) {
            IndexedSet set = training.getItems().get(idx);
            IndexedSet up = training.getItems().get(idx - 1);
            training.getItems().set(idx, up);
            training.getItems().set(idx - 1, set);
            training.getSelectionModel().select(idx - 1);
        }
    }

    @FXML
    private void onDown() {
        int idx = training.getSelectionModel().getSelectedIndex();
        if (idx < training.getItems().size() - 1) {
            IndexedSet set = training.getItems().get(idx);
            IndexedSet down = training.getItems().get(idx + 1);
            training.getItems().set(idx, down);
            training.getItems().set(idx + 1, set);
            training.getSelectionModel().select(idx + 1);
        }
    }

    @FXML
    private void onSave() {
        if (currentTraining == null) {
            currentTraining = new Training(date.getValue(), teams.getValue(), schedules.getValue());
        } else {
            currentTraining.setDate(date.getValue());
            currentTraining.setTeam(teams.getValue());
        }
        try {
            if (currentTraining.getId() == null) {
                db.getTblTraining().addTraining(currentTraining);
            } else {
                db.getTblTraining().updateTraining(currentTraining);
            }
            db.getTblTrainingContent().setSets(currentTraining, training.getItems());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "can't save training", e, eventBus);
        }
        close();
        eventBus.post(new TrainingEvent.TrainingSaveEvent(currentTraining));
    }

    @FXML
    private void onCancel() {
        close();
    }

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((TrainingEvent.TrainingOpenEvent) event).getObj());
    }

    private void populate(Training training) {
        currentTraining = training;
        populateTraining(training);
        populateSets();
    }

    private void populateTraining(Training tr) {
        if (tr != null) {
            date.setValue(tr.getDate());
            teams.setValue(tr.getTeam());
            schedules.setValue(tr.getSchedule());
            try {
                if (tr.getId() != null) {
                    List<IndexedSet> sets = db.getTblTrainingContent().getSets(tr);
                    training.getItems().setAll(sets);
                }
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, "Can't load sets for training", e, eventBus);
            }
        }
    }

    private void populateSets() {
        try {
            sets.getItems().setAll(db.getTblSet().getAllSets());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load sets", e, eventBus);
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
