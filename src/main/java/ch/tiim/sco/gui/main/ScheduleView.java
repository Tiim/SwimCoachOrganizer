package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.ScheduleEvent;
import ch.tiim.sco.gui.events.TeamEvent;
import ch.tiim.sco.gui.util.ModelCell;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ScheduleView extends MainView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleView.class);

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;

    @FXML
    private Parent root;
    @FXML
    private ListView<Team> teams;
    @FXML
    private ListView<ScheduleRule> schedules;
    @FXML
    private Spinner<Integer> days;
    @FXML
    private ListView<Pair<LocalDate, ScheduleRule>> nextTrainings;

    private BooleanProperty isTeamSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isScheduleSelected = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initMenu();
        days.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 4));
        isTeamSelected.bind(teams.getSelectionModel().selectedItemProperty().isNotNull());
        isScheduleSelected.bind(schedules.getSelectionModel().selectedItemProperty().isNotNull());
        teams.setCellFactory(param -> new ModelCell<>());
        schedules.setCellFactory(param -> new ModelCell<>());
        nextTrainings.setCellFactory(param -> new ScheduleCell());
        teams.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        days.valueProperty().addListener(observable -> populateNextTrainings());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem("Add Schedule", null, event -> onNew()),
                createItem("Delete Schedule", isScheduleSelected, event1 -> onDelete())
        );
    }

    private void selected(Team newValue) {
        if (newValue != null) {
            try {
                schedules.getItems().setAll(db.getTblSchedule().getSchedules(newValue));
            } catch (Exception e) {
                LOGGER.warn("Can't load schedule rules");
            }
        }
    }

    private void populateNextTrainings() {
        int d = days.getValue();
        try {
            nextTrainings.getItems().setAll(db.getProcSchedule().getNextTrainings(LocalDate.now(), d));
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load scheduled trainings", e, eventBus);
        }
    }

    private void populate() {
        try {
            teams.getItems().setAll(db.getTblTeam().getAllTeams());
            populateNextTrainings();
        } catch (Exception e) {
            LOGGER.warn("Can't load teams", e);
        }
    }

    private void onNew() {
        eventBus.post(new ScheduleEvent.ScheduleOpenEvent(null, mainStage));
    }

    private void onDelete() {
        ScheduleRule item = schedules.getSelectionModel().getSelectedItem();
        try {
            db.getTblSchedule().deleteSchedule(item);
        } catch (Exception e) {
            LOGGER.warn("Can't delete schedule");
        }
        eventBus.post(new ScheduleEvent.ScheduleDeleteEvent(item));
    }

    @Subscribe
    public void onTeam(TeamEvent event) {
        populate();
    }

    @Subscribe
    public void onSchedule(ScheduleEvent event) {
        selected(teams.getSelectionModel().getSelectedItem());
    }

    @Override
    public Parent getRoot() {
        return root;
    }


    private static class ScheduleCell extends ListCell<Pair<LocalDate, ScheduleRule>> {
        @Override
        protected void updateItem(Pair<LocalDate, ScheduleRule> item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(String.format("%s - %s %s", item.getKey(), item.getValue().getTeam().uiString(), item.getValue().getTime()));
            } else {
                setText("");
            }
        }
    }
}
