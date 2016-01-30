package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.Training;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.events.TrainingEvent;
import ch.tiim.sco.gui.util.ModelCell;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TrainingDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDialog.class);
    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private TextField name;
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

        colNr.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIndex()));
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getName()));
        colContent.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getContent()));
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
        boolean newTraining = false;
        if (currentTraining == null) {
            currentTraining = new Training(name.getText());
            newTraining = true;
        } else {
            currentTraining.setName(name.getText());
        }
        try {
            if (newTraining) {
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
            name.setText(tr.getName());
            try {
                List<IndexedSet> sets = db.getTblTrainingContent().getSets(tr);
                training.getItems().setAll(sets);
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
