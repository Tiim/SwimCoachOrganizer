package ch.tiim.sco.gui.team;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.utils.AddDeletePresenter;
import ch.tiim.sco.gui.utils.AddDeleteView;
import ch.tiim.sco.gui.utils.ModelCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TeamPresenter extends Page {
    private static final Logger LOGGER = LogManager.getLogger(TeamPresenter.class.getName());
    @FXML
    private ListView<Team> listTeams;
    @FXML
    private TextField fieldName;
    @FXML
    private TableView<Swimmer> tableMembers;
    @FXML
    private TableColumn<Swimmer, String> colFirstName;
    @FXML
    private TableColumn<Swimmer, String> colLastName;
    @FXML
    private TableColumn<Swimmer, String> colGender;
    @FXML
    private TableColumn<Swimmer, String> colAddress;
    @FXML
    private TableColumn<Swimmer, String> colEmail;
    @FXML
    private TableColumn<Swimmer, String> colBirthday;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<Team> teams = FXCollections.observableArrayList();
    private ObservableList<Swimmer> members = FXCollections.observableArrayList();

    @Inject
    private void injected() {
        updateTeams();
    }

    private void updateTeams() {
        int i = listTeams.getSelectionModel().getSelectedIndex();
        try {
            teams.setAll(db.getTblTeam().getAllTeams());
        } catch (Exception e) {
            LOGGER.warn("Error on loading teams", e);
        }
        listTeams.getSelectionModel().select(i);
    }

    @FXML
    private void initialize() {
        listTeams.setItems(teams);
        listTeams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                        updateMembers()
        );
        listTeams.setCellFactory(param1 -> new ModelCell<>());
        tableMembers.setItems(members);

        colFirstName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFirstName()));
        colLastName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastName()));
        colGender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isFemale() ? "F" : "M"));
        colAddress.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress()));
        colEmail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEmail()));
        colBirthday.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBirthDay().toString()));
    }

    private void updateMembers() {
        Team t = listTeams.getSelectionModel().getSelectedItem();
        if (t == null) {
            members.clear();
        } else {
            try {
                members.setAll(db.getTblTeamContent().getMembers(t));
            } catch (Exception e) {
                LOGGER.warn("Error on loading team members", e);
            }
        }
    }

    @FXML
    private void onBtnAdd() {
        if (!fieldName.getText().trim().isEmpty()) {
            try {
                db.getTblTeam().addTeam(new Team(fieldName.getText()));
            } catch (Exception e) {
                LOGGER.warn("Error on adding team", e);
            }
            updateTeams();
        }
    }

    @FXML
    private void onBtnEdit() {
        Team t = listTeams.getSelectionModel().getSelectedItem();
        if (t != null && !fieldName.getText().trim().isEmpty()) {
            t.setName(fieldName.getText());
            try {
                db.getTblTeam().updateTeam(t);
            } catch (Exception e) {
                LOGGER.warn("Error on updating team", e);
            }
            updateTeams();
        }
    }

    @FXML
    private void onBtnDelete() {
        if (listTeams.getSelectionModel().getSelectedItem() != null) {
            try {
                db.getTblTeam().deleteTeam(listTeams.getSelectionModel().getSelectedItem());
            } catch (Exception e) {
                LOGGER.warn("Error on deleting team", e);
            }
            updateTeams();
        }
    }

    @FXML
    private void onBtnMemberEdit() {
        Team t = listTeams.getSelectionModel().getSelectedItem();
        if (t != null) {
            AddDeletePresenter<Swimmer> v = new AddDeleteView<Swimmer>().getController();
            v.setRemove(swimmer -> db.getTblTeamContent().deleteMember(t, swimmer));
            v.setAdd(swimmer -> db.getTblTeamContent().addMember(t, swimmer));
            v.setIncludedFactory(() -> db.getTblTeamContent().getMembers(t));
            v.setExcludedFactory(() -> db.getTblTeamContent().getNotMembers(t));
            v.showAndWait();
        }
        updateMembers();
    }

    @Override
    public void opened() {
        updateTeams();
    }

    @Override
    public String getName() {
        return "Team";
    }
}
