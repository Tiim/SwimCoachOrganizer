package ch.tiim.sco.gui.training;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.database.model.SetForm;
import ch.tiim.sco.database.model.Training;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.addset.AddSetPresenter;
import ch.tiim.sco.gui.addset.AddSetView;
import ch.tiim.sco.gui.newtraining.NewTrainingPresenter;
import ch.tiim.sco.gui.newtraining.NewTrainingView;
import ch.tiim.sco.print.PrinterNode;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TrainingPresenter extends Page {
    private static final Logger LOGGER = LogManager.getLogger(TrainingPresenter.class.getName());

    @FXML
    private TableColumn<IndexedSet, Number> colIndex;
    @FXML
    private TableColumn<IndexedSet, String> colSetDistance;
    @FXML
    private TableColumn<IndexedSet, String> colSetContent;
    @FXML
    private TableColumn<IndexedSet, String> colSetName;
    @FXML
    private TableColumn<IndexedSet, Number> colSetIntensity;
    @FXML
    private TableColumn<IndexedSet, String> colSetFocus;
    @FXML
    private TableColumn<IndexedSet, String> colSetForm;
    @FXML
    private TableColumn<IndexedSet, String> colSetPause;

    @FXML
    private TableView<IndexedSet> tableTrainingContent;
    @FXML
    private ListView<Training> listTrainings;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<Training> trainings = FXCollections.observableArrayList();
    private ObservableList<IndexedSet> sets = FXCollections.observableArrayList();

    @Inject
    private void injected() {
        updateTrainingsList();
    }

    private void updateTrainingsList() {
        int i = listTrainings.getSelectionModel().getSelectedIndex();
        try {
            trainings.setAll(db.getTblTraining().getAllTrainings());
        } catch (Exception e) {
            LOGGER.warn("Error on loading trainings", e);
        }
        listTrainings.getSelectionModel().select(i);
    }

    @Override
    public void opened() {
        updateTrainingsList();
    }

    @Override
    public String getName() {
        return "Training";
    }

    @FXML
    private void initialize() {
        listTrainings.itemsProperty().setValue(trainings);
        listTrainings.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldVal, newVal) -> trainingSelected(newVal)
        );
        tableTrainingContent.setItems(sets);

        colIndex.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIndex()));
        colSetDistance.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getDistance()));
        colSetContent.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getContent()));
        colSetName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getName()));
        colSetIntensity.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getSet().getIntensity()));
        colSetFocus.setCellValueFactory(d -> {
            SetFocus f = d.getValue().getSet().getFocus();
            return new SimpleStringProperty(f != null ? f.getAbbr() : "-");
        });
        colSetForm.setCellValueFactory(d -> {
            SetForm f = d.getValue().getSet().getForm();
            return new SimpleStringProperty(f != null ? f.getAbbr() : "-");
        });
        colSetPause.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSet().getIntervalString()));
    }

    private void trainingSelected(Training newVal) {
        if (newVal != null) {
            try {
                sets.setAll(db.getTblTrainingContent().getSets(newVal));
            } catch (Exception e) {
                LOGGER.warn("Error on loading sets", e);
            }
        } else {
            sets.clear();
        }
    }

    @FXML
    private void onBtnDeleteTraining() {
        LOGGER.info("Delete training");
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        if (t == null) {
            return;
        }
        try {
            db.getTblTraining().deleteTraining(t);
        } catch (Exception e) {
            LOGGER.warn("Error on delete training", e);
        }
        updateTrainingsList();
    }

    @FXML
    private void onBtnNewTraining() {
        LOGGER.info("New Training");
        NewTrainingView view = new NewTrainingView();
        NewTrainingPresenter c = view.getController();
        if (c.showAndWait()) {
            try {
                db.getTblTraining().addTraining(new Training(c.getName()));
            } catch (Exception e) {
                LOGGER.warn("Error on adding training", e);
            }
            updateTrainingsList();
        }
    }

    @FXML
    private void onBtnAddSet() {
        LOGGER.info("Add set");
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        if (t == null) {
            return;
        }
        AddSetView v = new AddSetView();
        AddSetPresenter c = v.getController();
        if (c.showAndWait()) {
            int maxIndex = 0;
            for (IndexedSet s : sets) {
                if (maxIndex < s.getIndex()) {
                    maxIndex = s.getIndex();
                }
            }
            try {
                db.getTblTrainingContent().addSet(t, c.getSelectedSet(), maxIndex + 1);
            } catch (Exception e) {
                LOGGER.warn("Error on adding set", e);
            }
            updateSetsList();
        }
    }

    private void updateSetsList() {
        int i = tableTrainingContent.getSelectionModel().getSelectedIndex();
        trainingSelected(listTrainings.getSelectionModel().getSelectedItem());
        tableTrainingContent.getSelectionModel().select(i);
    }

    @FXML
    private void btnSetMoveUp() {
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        IndexedSet s = tableTrainingContent.getSelectionModel().getSelectedItem();
        if (t != null && s != null && s.getIndex() != 1) {
            try {
                db.getTblTrainingContent().updateIndex(t, s.getIndex(), true);
            } catch (Exception e) {
                LOGGER.warn("Error on updating index", e);
            }
            updateSetsList();
            tableTrainingContent.getSelectionModel().select(s.getIndex() - 2);
        }
    }

    @FXML
    private void btnSetMoveDown() {
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        IndexedSet s = tableTrainingContent.getSelectionModel().getSelectedItem();
        if (t != null && s != null && s.getIndex() != sets.size()) {
            try {
                db.getTblTrainingContent().updateIndex(t, s.getIndex(), false);
            } catch (Exception e) {
                LOGGER.warn("Error on updating index", e);
            }
            updateSetsList();
            tableTrainingContent.getSelectionModel().select(s.getIndex());
        }
    }

    @FXML
    private void btnSetDelete() {
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        IndexedSet s = tableTrainingContent.getSelectionModel().getSelectedItem();
        if (s == null || t == null) return;
        try {
            db.getTblTrainingContent().deleteSet(t, s.getSet(), s.getIndex());
        } catch (Exception e) {
            LOGGER.warn("Error on delete set", e);
        }
        updateSetsList();
    }

    @FXML
    private void onBtnPrint() {
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        PrinterNode node = new PrinterNode(t, sets);
        node.print();
    }

    @FXML
    private void onBtnPreview() {
        Training t = listTrainings.getSelectionModel().getSelectedItem();
        PrinterNode node = new PrinterNode(t, sets);
        node.show();
    }
}
