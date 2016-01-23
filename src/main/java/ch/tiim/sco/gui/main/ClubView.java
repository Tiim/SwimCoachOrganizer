package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.events.ClubEvent;
import ch.tiim.sco.gui.util.ModelCell;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ClubView extends MainView {

    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private ListView<Club> clubs;
    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label shortName;
    @FXML
    private Label country;
    @FXML
    private Label englishName;
    @FXML
    private Label officialCode;
    @FXML
    private Label shortEnglishName;
    @FXML
    private Label externId;
    @FXML
    private ListView<Team> teams;


    @FXML
    private void initialize() {
        clubs.setCellFactory(param -> new ModelCell<>());
        clubs.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));

        reload();
    }

    private void selected(Club newValue) {
        if (newValue != null) {
            name.setText(newValue.getName());
            type.setText(""); //FIXME
            shortName.setText(newValue.getNameShort());
            country.setText(newValue.getNationality());
            englishName.setText(newValue.getNameEn());
            officialCode.setText(newValue.getCode());
            shortEnglishName.setText(newValue.getNameShortEn());
            externId.setText(String.valueOf(newValue.getExternId()));
            try {
                teams.getItems().setAll(db.getTblClubContent().getTeams(newValue));
            } catch (Exception e) {
                LOGGER.warn("Can't load teams", e);
            }
        }
    }

    private void reload() {
        try {
            clubs.getItems().setAll(db.getTblClub().getAll());
        } catch (Exception e) {
            LOGGER.warn("Can't load clubs", e);
        }
    }

    @FXML
    void onDelete() {
        Club club = clubs.getSelectionModel().getSelectedItem();
        if (club != null) {
            try {
                db.getTblClub().deleteClub(club);
            } catch (Exception e) {
                LOGGER.warn("Can't delete club");
            }
            eventBus.post(new ClubEvent.ClubDeleteEvent(club));
        }
    }

    @FXML
    void onEdit() {
        Club club = clubs.getSelectionModel().getSelectedItem();
        if (club != null) {
            eventBus.post(new ClubEvent.ClubOpenEvent(club, mainStage));
        }
    }

    @FXML
    void onNew() {
        eventBus.post(new ClubEvent.ClubOpenEvent(null, mainStage));
    }

    @Subscribe
    public void onClub(ClubEvent event) {
        reload();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
