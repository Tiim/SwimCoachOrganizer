package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.export.ExportController;
import ch.tiim.sco.database.model.*;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.events.TrainingEvent;
import ch.tiim.sco.gui.util.ExportUtil;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.print.PrintTask;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TrainingView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingView.class);
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private ListView<Training> trainings;
    @FXML
    private TextField search;
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

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private FilteredList<Training> allTrainings;

    @FXML
    private void initialize() {
        initMenu();
        trainings.setCellFactory(param1 -> new ModelCell<>(lang));
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
        isSelected.bind(trainings.getSelectionModel().selectedItemProperty().isNotNull());
        selectedTraining.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) onDoubleClick();
        });
        search.textProperty().addListener(observable -> {
            if (search.getText() == null || search.getText().isEmpty()) {
                allTrainings.setPredicate(training -> true);
            } else {
                allTrainings.setPredicate(training ->
                        training.uiString(lang).toLowerCase().contains(search.getText().toLowerCase()));
            }
        });
        populate();
    }

    private void initMenu() {
        getMenu().getItems().setAll(
                createItem(lang.format("gui.export", "gui.training"), isSelected, event -> onExport()),
                createItem(lang.str("gui.export.pdf"), isSelected, event -> onPDF()),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.training"), null, event -> onNew()),
                createItem(lang.format("gui.edit", "gui.training"), isSelected, event -> onEdit()),
                createItem(lang.format("gui.delete", "gui.training"), isSelected, event -> onDelete())
        );
    }

    private void onExport() {
        new ExportUtil(db)
                .export(trainings.getSelectionModel().getSelectedItem(), mainStage, lang);
    }

    private void selected(Training newValue) {
        if (newValue != null) {
            try {
                selectedTraining.getItems().setAll(db.getTblTrainingContent().getSets(newValue));
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.set"), e);
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
            allTrainings = new FilteredList<>(FXCollections.observableArrayList(db.getTblTraining().getAllTrainings()),
                    training -> true);
            trainings.setItems(allTrainings);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.training"), e);
        }
    }

    private void onPDF() {
        Training item = trainings.getSelectionModel().getSelectedItem();
        if (item != null) {
            FileChooser fc = new FileChooser();
            fc.setInitialFileName("Training.pdf"); //NON-NLS
            File file = fc.showSaveDialog(mainStage);
            if (file != null) {
                eventBus.post(new PrintTask(item, file.toPath(), db, lang));
            }
        }
    }

    private void onNew() {
        eventBus.post(new TrainingEvent.TrainingOpenEvent(null, mainStage));
    }

    private void onEdit() {
        Training item = trainings.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new TrainingEvent.TrainingOpenEvent(item, mainStage));
        }
    }

    private void onDelete() {
        Training item = trainings.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblTraining().deleteTraining(item);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.training"), e);
            }
            eventBus.post(new TrainingEvent.TrainingDeleteEvent(item));
        }
    }

    @Subscribe
    public void onTraining(TrainingEvent event) {
        populate();
        if (event.getObj() != null && event.getObj().getId() != null) {
            trainings.getSelectionModel().select(event.getObj());
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
