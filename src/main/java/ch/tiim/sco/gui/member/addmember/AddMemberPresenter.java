package ch.tiim.sco.gui.member.addmember;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddMemberPresenter {
    private static final Logger LOGGER = LogManager.getLogger(AddMemberPresenter.class.getName());
    @FXML
    private ListView<Swimmer> listExcluded;
    @FXML
    private ListView<Swimmer> listIncluded;
    @FXML
    private Parent root;

    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "team")
    private Team team;

    private ObservableList<Swimmer> included = FXCollections.observableArrayList();
    private ObservableList<Swimmer> excluded = FXCollections.observableArrayList();


    private Stage stage;


    @FXML
    private void initialize() {
        listExcluded.setItems(excluded);
        listIncluded.setItems(included);
    }

    @Inject
    private void injected() {
        stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Select Set");

        updateExcluded();
        updateIncluded();
    }

    private void updateExcluded() {
        try {
            excluded.setAll(db.getTblTeamContent().getNotMembers(team));
        } catch (Exception e) {
            LOGGER.warn("Error while loading non members", e);
        }
    }

    private void updateIncluded() {
        try {
            included.setAll(db.getTblTeamContent().getMembers(team));
        } catch (Exception e) {
            LOGGER.warn("Error while loading members", e);
        }
    }

    @FXML
    void onBtnAdd() {
        Swimmer m = listExcluded.getSelectionModel().getSelectedItem();
        if (m != null) {
            try {
                db.getTblTeamContent().addMember(team, m);
            } catch (Exception e) {
                LOGGER.warn("Error while adding member", e);
            }
        }
        updateExcluded();
        updateIncluded();
    }

    @FXML
    void onBtnOk() {
        stage.close();
    }

    @FXML
    void onBtnRemove() {
        Swimmer m = listIncluded.getSelectionModel().getSelectedItem();
        if (m != null) {
            try {
                db.getTblTeamContent().deleteMember(team, m);
            } catch (Exception e) {
                LOGGER.warn("Error while deleting member", e);
            }
        }
        updateExcluded();
        updateIncluded();
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
