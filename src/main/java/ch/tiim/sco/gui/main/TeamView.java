package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.event.EmailEvent;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.TeamEvent;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.OutOfCoffeeException;
import com.google.common.eventbus.Subscribe;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamView.class);
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "host")
    private HostServices host;

    @FXML
    private SplitPane root;
    @FXML
    private ListView<Team> teams;
    @FXML
    private TableView<Swimmer> swimmers;
    @FXML
    private TableColumn<Swimmer, String> firstName;
    @FXML
    private TableColumn<Swimmer, String> name;
    @FXML
    private TableColumn<Swimmer, String> gender;
    @FXML
    private TableColumn<Swimmer, String> address;
    @FXML
    private TableColumn<Swimmer, String> email;
    @FXML
    private TableColumn<Swimmer, String> birthday;


    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initMenu();
        teams.setCellFactory(param1 -> new ModelCell<>());
        teams.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> select(newValue));
        firstName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFirstName()));
        name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastName()));
        gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isFemale() ? "F" : "M"));
        address.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress()));
        email.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEmail()));
        birthday.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBirthDay().toString()));
        isSelected.bind(teams.getSelectionModel().selectedItemProperty().isNotNull());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem("Export Team", isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented yet");
                }),
                new SeparatorMenuItem(),
                createItem("Sent E-Mail To All Team Members", isSelected, event1 -> onEmail()),
                new SeparatorMenuItem(),
                createItem("New Team", null, event2 -> onNew()),
                createItem("Edit Team", isSelected, event3 -> onEdit()),
                createItem("Delete Team", isSelected, event4 -> onDelete())
        );
    }

    private void select(Team team) {
        if (team != null) {
            try {
                swimmers.getItems().setAll(db.getTblTeamContent().getSwimmers(team));
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, "Can't load swimmers", e, eventBus);
            }
        }
    }

    private void populate() {
        try {
            teams.getItems().setAll(db.getTblTeam().getAllTeams());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load teams", e, eventBus);
        }
    }

    private void onEmail() {
        Team t = teams.getSelectionModel().getSelectedItem();
        try {
            if (t != null) {
                EmailEvent event = new EmailEvent(db.getTblTeamContent().getSwimmers(t));
                event.setOnSucceeded(event1 -> host.showDocument(event.getValue()));
                eventBus.post(event);
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load swimmers", e, eventBus);
        }
    }

    private void onNew() {
        eventBus.post(new TeamEvent.TeamOpenEvent(null, mainStage));
    }

    private void onEdit() {
        Team team = teams.getSelectionModel().getSelectedItem();
        if (team != null) {
            eventBus.post(new TeamEvent.TeamOpenEvent(team, mainStage));
        }
    }

    private void onDelete() {
        Team team = teams.getSelectionModel().getSelectedItem();
        if (team != null) {
            try {
                db.getTblTeam().deleteTeam(team);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, "Can't delete team", e, eventBus);
            }
            eventBus.post(new TeamEvent.TeamDeleteEvent(team));
        }
    }

    @Subscribe
    public void onTeam(TeamEvent event) {
        populate();
        teams.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
