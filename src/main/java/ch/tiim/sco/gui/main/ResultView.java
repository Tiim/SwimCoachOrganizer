package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.events.ResultEvent;
import ch.tiim.sco.gui.util.DurationTableCell;
import ch.tiim.sco.gui.util.ModelCell;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.Duration;

public class ResultView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;

    @FXML
    private Parent root;
    @FXML
    private ListView<Swimmer> swimmers;
    @FXML
    private TableView<Result> results;
    @FXML
    private TableColumn<Result, String> distance;
    @FXML
    private TableColumn<Result, String> stroke;
    @FXML
    private TableColumn<Result, Duration> time;
    @FXML
    private TableColumn<Result, String> course;
    @FXML
    private TableColumn<Result, String> date;
    @FXML
    private TableColumn<Result, String> meet;
    @FXML
    private TableColumn<Result, Duration> reactionTime;

    @FXML
    private void initialize() {
        swimmers.setCellFactory(param1 -> new ModelCell<>());
        swimmers.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        distance.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getDistance())));
        stroke.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStroke().toString()));
        time.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSwimTime()));
        course.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCourse().toString()));
        date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMeetDate().toString()));
        meet.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMeet()));
        reactionTime.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getReactionTime()));
        time.setCellFactory(param -> new DurationTableCell());
        reactionTime.setCellFactory(param -> new DurationTableCell());
        populate();
    }

    private void selected(Swimmer swimmer) {
        if (swimmer != null) {
            try {
                results.getItems().setAll(db.getTblResult().getResults(swimmer));
            } catch (Exception e) {
                LOGGER.warn("Can't load results", e);
            }
        }
    }

    private void populate() {
        try {
            swimmers.getItems().setAll(db.getTblSwimmer().getAllSwimmers());
        } catch (Exception e) {
            LOGGER.warn("Can't load swimmers", e);
        }
    }

    @FXML
    private void onDelete() {
        Swimmer swimmer = swimmers.getSelectionModel().getSelectedItem();
        Result res = results.getSelectionModel().getSelectedItem();
        if (res != null && swimmer != null) {
            try {
                db.getTblResult().deleteResult(res);
            } catch (Exception e) {
                LOGGER.warn("Can't delete result", e);
            }
            eventBus.post(new ResultEvent.ResultDeleteEvent(res, swimmer));
        }
    }

    @FXML
    private void onEdit() {
        Swimmer swimmer = swimmers.getSelectionModel().getSelectedItem();
        Result res = results.getSelectionModel().getSelectedItem();
        if (res != null && swimmer != null) {
            eventBus.post(new ResultEvent.ResultOpenEvent(res, swimmer, mainStage));
        }
    }

    @FXML
    private void onNew() {
        Swimmer swimmer = swimmers.getSelectionModel().getSelectedItem();
        if (swimmer != null) {
            eventBus.post(new ResultEvent.ResultOpenEvent(null, swimmer, mainStage));
        }
    }

    @FXML
    private void onImport() {
        eventBus.post(new ResultEvent.ResultImportEvent(mainStage));
    }

    @Subscribe
    public void onResult(ResultEvent event) {
        populate();
        swimmers.getSelectionModel().select(event.getSwimmer());
//        results.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
