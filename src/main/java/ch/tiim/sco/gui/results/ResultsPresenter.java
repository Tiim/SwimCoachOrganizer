package ch.tiim.sco.gui.results;

import ch.tiim.inject.Inject;
import ch.tiim.javafx.ValidationListener;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Course;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Stroke;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.utils.ModelCell;
import ch.tiim.sco.util.DurationFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDate;


public class ResultsPresenter extends Page {
    private static final String PATTERN_TIME = "((\\d+:)?\\d{1,2}:)?\\d{1,2}\\.\\d{1,2}";
    private static final String PATTERN_TIME_OPT = "(((\\d+:)?\\d{1,2}:)?\\d{1,2}\\.\\d{1,2})?";
    private static final Logger LOGGER = LogManager.getLogger(ResultsPresenter.class.getName());

    @FXML
    private ListView<Swimmer> listSwimmers;
    @FXML
    private TableView<Result> tableResults;
    @FXML
    private TableColumn<Result, String> colStyle;
    @FXML
    private TableColumn<Result, String> colTime;
    @FXML
    private TableColumn<Result, String> colDate;
    @FXML
    private TableColumn<Result, String> colMeet;
    @FXML
    private TableColumn<Result, Course> colCourse;
    @FXML
    private TextField fieldMeet;
    @FXML
    private DatePicker date;
    @FXML
    private TextField fieldTime;
    @FXML
    private TextField fieldReactionTime;
    @FXML
    private ChoiceBox<Stroke> choiceStroke;
    @FXML
    private Spinner<Integer> spinnerDistance;
    @FXML
    private Label labelResultString;
    @FXML
    private ChoiceBox<Course> choiceCourse;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<Swimmer> swimmers = FXCollections.observableArrayList();
    private ObservableList<Result> results = FXCollections.observableArrayList();


    @Inject
    private void injected() {

    }

    @FXML
    private void initialize() {
        spinnerDistance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 100));
        ObservableList<Stroke> stroke = FXCollections.observableArrayList();
        ObservableList<Course> courses = FXCollections.observableArrayList();
        stroke.setAll(Stroke.values());
        courses.setAll(Course.values());
        choiceStroke.setItems(stroke);
        choiceCourse.setItems(courses);
        listSwimmers.setItems(swimmers);
        tableResults.setItems(results);
        listSwimmers.setCellFactory(param -> new ModelCell<>());
        listSwimmers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                        selectSwimmer(newValue)
        );
        tableResults.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                        selectResult(newValue)
        );
        colStyle.setCellValueFactory(param ->
                        new SimpleStringProperty(
                                String.format("%dm %s", param.getValue().getDistance(), param.getValue().getStroke())
                        )
        );
        colCourse.setCellValueFactory(param -> new SimpleObjectProperty<Course>(param.getValue().getCourse()));
        colTime.setCellValueFactory(param ->
                new SimpleStringProperty(DurationFormatter.format(param.getValue().getSwimTime())));
        colDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMeetDate().toString()));
        colMeet.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMeet()));
        fieldTime.textProperty().addListener(new ValidationListener(PATTERN_TIME, fieldTime));
        fieldReactionTime.textProperty().addListener(new ValidationListener(PATTERN_TIME_OPT, fieldReactionTime));
    }

    @FXML
    private void onBtnAdd() {
        Result r = getResultFromFields();
        Swimmer s = listSwimmers.getSelectionModel().getSelectedItem();
        if (r != null && s != null) {
            try {
                db.getTblResult().addResult(s, r);
            } catch (Exception e) {
                LOGGER.warn("Error while adding result", e);
            }
            updateResults();
        }
    }

    @FXML
    private void onBtnEdit() {
        Result original = tableResults.getSelectionModel().getSelectedItem();
        Result r = getResultFromFields();

        if (original != null && r != null) {
            r.setId(original.getId());
            try {
                db.getTblResult().updateResult(r);
            } catch (Exception e) {
                LOGGER.warn("Error while updating result", e);
            }
            updateResults();
        }
    }

    @FXML
    private void onBtnDelete() {
        Result original = tableResults.getSelectionModel().getSelectedItem();
        if (original != null) {
            try {
                db.getTblResult().deleteResult(original);
            } catch (Exception e) {
                LOGGER.warn("Error while deleting result", e);
            }
            updateResults();
        }
    }

    private Result getResultFromFields() {
        String meet = fieldMeet.getText();
        LocalDate d = date.getValue();
        Duration time;
        try {
            time = DurationFormatter.parse(fieldTime.getText());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Can't parse swim time");
            return null;
        }
        Duration reaction = null;
        try {
            reaction = DurationFormatter.parse(fieldReactionTime.getText());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Can't parse reaction time");
        }
        Stroke str = choiceStroke.getValue();
        int distance = spinnerDistance.getValue();
        Course c = choiceCourse.getValue();
        return new Result(
                meet, d, time, reaction, str, distance, c
        );
    }

    private void selectResult(Result r) {
        if (r != null) {
            fieldMeet.setText(r.getMeet());
            date.setValue(r.getMeetDate());
            fieldTime.setText(DurationFormatter.format(r.getSwimTime()));
            fieldReactionTime.setText(DurationFormatter.format(r.getReactionTime()));
            choiceStroke.setValue(r.getStroke());
            labelResultString.setText(r.uiString());
            spinnerDistance.getValueFactory().setValue(r.getDistance());
            choiceCourse.setValue(r.getCourse());
        }
    }

    private void selectSwimmer(Swimmer newValue) {
        if (newValue != null) {
            try {
                results.setAll(db.getTblResult().getResults(newValue));
            } catch (Exception e) {
                LOGGER.warn("Error while loading results", e);
            }
        }
    }

    private void updateResults() {
        try {
            Swimmer s = listSwimmers.getSelectionModel().getSelectedItem();
            if (s != null) {
                int i = tableResults.getSelectionModel().getSelectedIndex();
                results.setAll(db.getTblResult().getResults(s));
                tableResults.getSelectionModel().select(i);
            }
        } catch (Exception e) {
            LOGGER.warn("Error while updating swimmers", e);
        }
    }

    private void updateSwimmers() {
        try {
            int i = listSwimmers.getSelectionModel().getSelectedIndex();
            swimmers.setAll(db.getTblSwimmer().getAllSwimmers());
            listSwimmers.getSelectionModel().select(i);
        } catch (Exception e) {
            LOGGER.warn("Error while updating swimmers", e);
        }
    }

    @Override
    public void opened() {
        updateSwimmers();
    }

    @Override
    public String getName() {
        return "Results";
    }
}
