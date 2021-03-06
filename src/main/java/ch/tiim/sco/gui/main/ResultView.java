package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.ResultEvent;
import ch.tiim.sco.gui.util.DurationTableCell;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ResultView extends MainView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultView.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private TextField search;
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

    private BooleanProperty isSwimmerSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isResultSelected = new SimpleBooleanProperty(false);

    private FilteredList<Swimmer> allSwimmers;

    @FXML
    private void initialize() {
        initMenu();
        swimmers.setCellFactory(param1 -> new ModelCell<>(lang));
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
        isSwimmerSelected.bind(swimmers.getSelectionModel().selectedItemProperty().isNotNull());
        isResultSelected.bind(results.getSelectionModel().selectedItemProperty().isNotNull());
        search.textProperty().addListener(observable -> {
            if (search.getText() == null || search.getText().isEmpty()) {
                allSwimmers.setPredicate(set -> true);
            } else {
                allSwimmers.setPredicate(set -> set.uiString(lang).toLowerCase().contains(search.getText().toLowerCase()));
            }
        });
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem(lang.format("gui.export", "gui.result"), isSwimmerSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented yet");
                }),
                createItem(lang.str("gui.import.result"), null, event1 -> onImport()),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.result"), isSwimmerSelected, event2 -> onNew()),
                createItem(lang.format("gui.edit", "gui.result"), isResultSelected, event3 -> onEdit()),
                createItem(lang.format("gui.delete", "gui.result"), isResultSelected, event4 -> onDelete())
        );
    }

    private void selected(Swimmer swimmer) {
        if (swimmer != null) {
            try {
                results.getItems().setAll(db.getTblResult().getResults(swimmer));
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.result"), e);
            }
        }
    }

    private void populate() {
        try {
            allSwimmers = new FilteredList<>(FXCollections.observableArrayList(db.getTblSwimmer().getAllSwimmers()));
            swimmers.setItems(allSwimmers);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.swimmer"), e);
        }
    }

    private void onImport() {
        eventBus.post(new ResultEvent.ResultImportEvent(mainStage));
    }

    private void onNew() {
        Swimmer swimmer = swimmers.getSelectionModel().getSelectedItem();
        if (swimmer != null) {
            eventBus.post(new ResultEvent.ResultOpenEvent(null, swimmer, mainStage));
        }
    }

    private void onEdit() {
        Swimmer swimmer = swimmers.getSelectionModel().getSelectedItem();
        Result res = results.getSelectionModel().getSelectedItem();
        if (res != null && swimmer != null) {
            eventBus.post(new ResultEvent.ResultOpenEvent(res, swimmer, mainStage));
        }
    }

    private void onDelete() {
        Swimmer swimmer = swimmers.getSelectionModel().getSelectedItem();
        Result res = results.getSelectionModel().getSelectedItem();
        if (res != null && swimmer != null) {
            try {
                db.getTblResult().deleteResult(res);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.result"), e);
            }
            eventBus.post(new ResultEvent.ResultDeleteEvent(res, swimmer));
        }
    }

    @Subscribe
    public void onResult(ResultEvent event) {
        populate();
        swimmers.getSelectionModel().select(event.getSwimmer());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
