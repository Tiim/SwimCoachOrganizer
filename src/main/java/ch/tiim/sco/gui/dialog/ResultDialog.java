package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Course;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Stroke;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.component.DurationField;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.ResultEvent;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultDialog.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private Spinner<Integer> distance;
    @FXML
    private ChoiceBox<Stroke> stroke;
    @FXML
    private DurationField time;
    @FXML
    private DurationField reactionTime;
    @FXML
    private ChoiceBox<Course> course;
    @FXML
    private DatePicker date;
    @FXML
    private TextField meet;
    private Result currentResult;
    private Swimmer currentSwimmer;

    @FXML
    private void initialize() {
        distance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 100, 25));
        stroke.getItems().setAll(Stroke.values());
        course.getItems().setAll(Course.values());
    }

    @FXML
    private void onSave() {
        try {
            currentResult = getResult();
        } catch (UIException e) {
            e.showDialog(lang.str("gui.missing"));
        }
        try {
            if (currentResult.getId() == null) {
                db.getTblResult().addResult(currentSwimmer, currentResult);
            } else {
                db.getTblResult().updateResult(currentResult);
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.save", "error.subj.result"), e);
        }
        eventBus.post(new ResultEvent.ResultSaveEvent(currentResult, currentSwimmer));
        close();
    }

    private Result getResult() throws UIException {
        if (currentResult == null) {
            currentResult = new Result();
        }
        currentResult.setDistance(distance.getValue());
        currentResult.setStroke(stroke.getValue());
        try {
            currentResult.setSwimTime(time.getDuration());
        } catch (IllegalArgumentException e) {
            throw new UIException(lang.str("gui.time_format"));
        }
        currentResult.setReactionTime(reactionTime.getDuration());
        currentResult.setCourse(course.getValue());
        currentResult.setMeetDate(date.getValue());
        return currentResult;
    }

    @FXML
    private void onCancel() {
        close();
    }

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        ResultEvent.ResultOpenEvent resEvent = (ResultEvent.ResultOpenEvent) event;
        populate(resEvent.getObj(), resEvent.getSwimmer());
    }

    private void populate(Result result, Swimmer swimmer) {
        currentResult = result;
        currentSwimmer = swimmer;
        if (result != null) {
            distance.getValueFactory().setValue(result.getDistance());
            stroke.setValue(result.getStroke());
            time.setDuration(result.getSwimTime());
            reactionTime.setDuration(result.getReactionTime());
            course.setValue(result.getCourse());
            date.setValue(result.getMeetDate());
            meet.setText(result.getMeet());
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
