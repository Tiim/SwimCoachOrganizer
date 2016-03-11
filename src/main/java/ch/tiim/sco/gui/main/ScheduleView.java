package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.database.model.Training;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.component.CalendarControl;
import ch.tiim.sco.gui.events.ScheduleEvent;
import ch.tiim.sco.gui.events.TeamEvent;
import ch.tiim.sco.gui.events.TrainingEvent;
import ch.tiim.sco.gui.util.ExportUtil;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.ColorUtil;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleView extends MainView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleView.class);

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private ListView<Team> teams;
    @FXML
    private TextField search;
    @FXML
    private ListView<ScheduleRule> schedules;
    @FXML
    private CalendarControl<ScheduleRule> calendar;

    private BooleanProperty isTeamSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isScheduleSelected = new SimpleBooleanProperty(false);
    private FilteredList<Team> allTeams;

    @FXML
    private void initialize() {
        initMenu();
        isTeamSelected.bind(teams.getSelectionModel().selectedItemProperty().isNotNull());
        isScheduleSelected.bind(schedules.getSelectionModel().selectedItemProperty().isNotNull());
        teams.setCellFactory(param -> new ModelCell<>(lang));
        schedules.setCellFactory(param -> new ModelCell<>(lang));
        teams.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        calendar.setCallback(this::getEvents);
        calendar.setOnEventCallback(this::onEventClicked);
        search.textProperty().addListener(observable -> {
            if (search.getText() == null || search.getText().isEmpty()) {
                allTeams.setPredicate(set -> true);
            } else {
                allTeams.setPredicate(set -> set.uiString(lang).toLowerCase().contains(search.getText().toLowerCase()));
            }
        });
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem(lang.format("gui.export", "gui.schedule"), isScheduleSelected, it -> onExport()),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.schedule"), null, event -> onNew()),
                createItem(lang.format("gui.delete", "gui.schedule"), isScheduleSelected, event1 -> onDelete())
        );
    }

    private void onExport() {
        new ExportUtil(db).export(schedules.getSelectionModel().getSelectedItem(), mainStage, lang);

    }

    private void selected(Team newValue) {
        if (newValue != null) {
            try {
                schedules.getItems().setAll(db.getTblSchedule().getSchedules(newValue));
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.schedule"), e);
            }
        }
    }

    private void populate() {
        try {
            allTeams = new FilteredList<>(FXCollections.observableArrayList(db.getTblTeam().getAllTeams()));
            teams.setItems(allTeams);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.team"), e);
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
            ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.schedule"), e);
        }
        eventBus.post(new ScheduleEvent.ScheduleDeleteEvent(item));
    }

    private void onEventClicked(LocalDate ld, ScheduleRule scheduleRule) {
        Training t = null;
        try {
            t = db.getTblTraining().getTrainingFromSchedule(ld, scheduleRule);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.schedule"), e);
        }
        if (t == null) {
            eventBus.post(new TrainingEvent.TrainingOpenEvent(
                    new Training(ld, scheduleRule.getTeam(), scheduleRule), mainStage));
        } else {
            eventBus.post(new TrainingEvent.TrainingOpenEvent(t, mainStage));
        }
    }

    private List<CalendarControl.CalendarEvent<ScheduleRule>> getEvents(LocalDate localDate) {
        try {
            return db.getProcSchedule().getTrainingsForDay(localDate).stream()
                    .map(it -> new CalendarControl.CalendarEvent<>(it.getTime(), it.getTeam().getName(),
                            ColorUtil.getPastelColorHash(it.getTeam().getName()), it)).collect(Collectors.toList());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.training"), e);
            return new ArrayList<>(0);
        }
    }

    @Subscribe
    public void onTeam(TeamEvent event) {
        populate();
    }

    @Subscribe
    public void onSchedule(ScheduleEvent event) {
        calendar.reloadEvents();
        selected(teams.getSelectionModel().getSelectedItem());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
