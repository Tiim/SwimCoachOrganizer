package ch.tiim.sco.gui.lenex;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.lenex.ImportReultsTask;
import ch.tiim.sco.lenex.LenexLoadTask;
import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.util.FileChooserUtil;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.file.Path;

public class LenexPresenter extends Page {
    private static final Logger LOGGER = LogManager.getLogger(LenexPresenter.class.getName());
    private static final FileChooser.ExtensionFilter LENEX_EXT = new FileChooser.ExtensionFilter("LENEX File", "*.lxf", "*.lef");

    @FXML
    private Label path;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label lblApp;
    @FXML
    private Label lblName;
    @FXML
    private Label lblContact;
    @FXML
    private ListView<Pair<Swimmer, Result>> resultList;

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "event-bus")
    private EventBus eventBus;
    @Inject(name = "main-stage")
    private Stage stage;

    private Lenex lenex;
    private ObservableList<Pair<Swimmer, Result>> results = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        resultList.setItems(results);
        resultList.setOnKeyReleased(event -> {
            int i;
            if (event.getCode().equals(KeyCode.DELETE) &&
                    (i = resultList.getSelectionModel().getSelectedIndex()) >= 0) {
                results.remove(i);
            }
        });
    }

    @FXML
    private void onBtnClose() {

    }

    @FXML
    private void onBtnImportMeet() {

    }

    @FXML
    private void onBtnImportResults() {
        if (lenex != null) {
            try {
                ImportReultsTask task = new ImportReultsTask(db.getTblSwimmer().getAllSwimmers(), lenex);
                task.setOnSucceeded(event -> results.setAll(task.getValue()));
                eventBus.post(task);
            } catch (Exception e) {
                LOGGER.warn("Error on importing results", e);
            }
        }
    }

    @FXML
    private void onBtnImportResultsFinal() {
        for (Pair<Swimmer, Result> r : results) {
            try {
                db.getTblResult().addResult(r.getKey(), r.getValue());
            } catch (Exception e) {
                LOGGER.warn("Error on importing results", e);
            }
        }
        results.clear();
    }

    @FXML
    private void onBtnOpen() {
        Path p = FileChooserUtil.openFile(stage, LENEX_EXT);
        LenexLoadTask lenexLoadTask = new LenexLoadTask(p);
        lenexLoadTask.setOnSucceeded(event -> {
            lenex = lenexLoadTask.getValue();
            lenexChanged();
            progress.setVisible(false);
        });
        eventBus.post(lenexLoadTask);
        progress.setVisible(true);
        path.setText(p.toString());
    }

    private void lenexChanged() {
        if (lenex != null) {
            lblApp.setText(lenex.constructor.name + " " + lenex.constructor.version);
            lblName.setText(lenex.constructor.registration);
            lblContact.setText(lenex.constructor.contact.email);
        } else {
            lblApp.setText("");
            lblName.setText("");
            lblContact.setText("");
        }
    }

    @Override
    public void opened() {
    }

    @Override
    public InputStream getIcon() {
        return LenexPresenter.class.getResourceAsStream("icon3.png");
    }

    @Override
    public String getName() {
        return "Lenex";
    }
}
