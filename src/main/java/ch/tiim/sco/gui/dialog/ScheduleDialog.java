package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.ScheduleEvent;
import ch.tiim.sco.gui.util.ModelConverter;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.gui.util.Validator;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDialog.class);


    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private ChoiceBox<Team> team;
    @FXML
    private DatePicker startDay;
    @FXML
    private Spinner<Integer> startHour;
    @FXML
    private Spinner<Integer> startMinute;
    @FXML
    private Spinner<Integer> duration;
    @FXML
    private Spinner<Integer> interval;
    @FXML
    private Label text;

    @FXML
    private void initialize() {
        try {
            team.getItems().setAll(db.getTblTeam().getAllTeams());
        } catch (Exception e) {
            LOGGER.warn("Can't load teams", e);
        }
        startHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 18));
        startMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 15));
        duration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 90));
        interval.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 7));
        team.setConverter(new ModelConverter<>());
        InvalidationListener l = observable -> onChange();

        team.valueProperty().addListener(l);
        startDay.valueProperty().addListener(l);
        startHour.valueProperty().addListener(l);
        startMinute.valueProperty().addListener(l);
        duration.valueProperty().addListener(l);
        interval.valueProperty().addListener(l);
    }

    private void onChange() {
        try {
            text.setText(getScheduleRule().uiString());
        } catch (Exception e) {
            //Ignore
        }
    }

    private ScheduleRule getScheduleRule() throws UIException {
        LocalDate day = Validator.nonNull(startDay.getValue(), "Start Day");
        LocalTime time = LocalTime.of(startHour.getValue(), startMinute.getValue());
        Team team = Validator.nonNull(this.team.getValue(), "Team");
        return new ScheduleRule(day, time, interval.getValue(), duration.getValue(), team);
    }

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((ScheduleEvent.ScheduleOpenEvent) event).getObj());
    }

    private void populate(ScheduleRule obj) {
        if (obj != null) {
            team.setValue(obj.getTeam());
            startDay.setValue(obj.getStart());
            startHour.getValueFactory().setValue(obj.getTime().getHour());
            startMinute.getValueFactory().setValue(obj.getTime().getMinute());
        } else {
            startDay.setValue(LocalDate.now());
        }
    }

    @FXML
    private void onSave() {
        try {
            ScheduleRule schedule = getScheduleRule();
            db.getTblSchedule().addSchedule(schedule);
            eventBus.post(new ScheduleEvent.ScheduleSaveEvent(schedule));
            close();
        } catch (UIException e) {
            e.showDialog("Missing settings:");
        } catch (Exception e) {
            LOGGER.warn("Can't save schedule", e);
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
