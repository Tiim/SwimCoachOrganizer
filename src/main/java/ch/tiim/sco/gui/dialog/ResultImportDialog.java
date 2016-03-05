package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Stroke;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.ResultEvent;
import ch.tiim.sco.lenex.ImportResultsTask;
import ch.tiim.sco.lenex.LenexLoadTask;
import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.util.DurationFormatter;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ResultImportDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultImportDialog.class);
    private final HashMap<Pair<Swimmer, Result>, BooleanProperty> selected = new HashMap<>();
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private BorderPane root;
    @FXML
    private TextField path;
    @FXML
    private TableView<Pair<Swimmer, Result>> results;
    @FXML
    private TableColumn<Pair<Swimmer, Result>, Boolean> isImport;
    @FXML
    private TableColumn<Pair<Swimmer, Result>, String> swimmer;
    @FXML
    private TableColumn<Pair<Swimmer, Result>, String> time;
    @FXML
    private TableColumn<Pair<Swimmer, Result>, Number> distance;
    @FXML
    private TableColumn<Pair<Swimmer, Result>, Stroke> stroke;

    @FXML
    private void initialize() {
        isImport.setCellFactory(CheckBoxTableCell.forTableColumn(isImport));
        isImport.setCellValueFactory(param1 -> selected.get(param1.getValue()));
        swimmer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().uiString()));
        time.setCellValueFactory(param ->
                new SimpleStringProperty(DurationFormatter.format(param.getValue().getValue().getSwimTime())));
        distance.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getDistance()));
        stroke.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getStroke()));
        path.textProperty().addListener((observable, oldValue, newValue) -> pathChanged(newValue));
    }

    private void pathChanged(String str) {
        Path path;
        try {
            path = Paths.get(str);
        } catch (InvalidPathException ignored) {
            return;
        }
        if (path != null && Files.isRegularFile(path)) {
            LenexLoadTask loadTask = new LenexLoadTask(path);
            loadTask.setOnSucceeded(event -> onLenexLoaded(loadTask.getValue()));
            loadTask.setOnFailed(event ->
                    ExceptionAlert.showError(LOGGER,
                            lang.format("error.load", "error.subj.lenex"), event.getSource().getException()));
            eventBus.post(loadTask);
        }
    }

    private void onLenexLoaded(Lenex lenex) {
        List<Swimmer> swimmers;
        try {
            swimmers = db.getTblSwimmer().getAllSwimmers();
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.swimmer"), e);
            return;
        }
        ImportResultsTask importTask = new ImportResultsTask(swimmers, lenex);
        importTask.setOnSucceeded(event -> onResultsLoaded(importTask.getValue()));
        importTask.setOnFailed(event ->
                ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.lenex"),
                        event.getSource().getException()));
        eventBus.post(importTask);
    }

    private void onResultsLoaded(List<Pair<Swimmer, Result>> value) {
        for (Pair<Swimmer, Result> pair : value) {
            selected.put(pair, new SimpleBooleanProperty(true));
        }
        results.getItems().setAll(value);
    }

    @FXML
    private void onBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(lang.str("gui.open.lenex"));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("LENEX", "*.lxf", "*.lef"));
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            path.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    @FXML
    private void onSave() {
        List<Pair<Swimmer, Result>> s = selected.keySet().stream()
                .filter(res -> selected.get(res).getValue())
                .collect(Collectors.toList());
        try {
            for (Pair<Swimmer, Result> sw : s) {
                db.getTblResult().addResult(sw.getKey(), sw.getValue());
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.save", "error.subj.result"), e);
        }
        if (!s.isEmpty()) {
            eventBus.post(new ResultEvent.ResultSaveEvent(s.get(0).getValue(), s.get(0).getKey()));
        }
        close();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
