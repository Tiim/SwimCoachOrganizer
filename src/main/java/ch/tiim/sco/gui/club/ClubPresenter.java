package ch.tiim.sco.gui.club;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.utils.AddDeletePresenter;
import ch.tiim.sco.gui.utils.AddDeleteView;
import ch.tiim.sco.gui.utils.ModelCell;
import ch.tiim.sco.lenex.model.Nation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class ClubPresenter extends Page {
    private static final Logger LOGGER = LogManager.getLogger(ClubPresenter.class.getName());
    @FXML
    private ListView<Club> listClubs;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldShortName;
    @FXML
    private TextField fieldEnglishName;
    @FXML
    private ChoiceBox<Nation> choiceNationality;
    @FXML
    private TextField fieldClubCode;
    @FXML
    private ChoiceBox<ch.tiim.sco.lenex.model.Club.TypeClub> choiceType;
    @FXML
    private Spinner<Integer> spinnerClubId;
    @FXML
    private TextField fieldEnglishShortName;
    @FXML
    private ListView<Team> listTeams;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<Team> teams = FXCollections.observableArrayList();
    private ObservableList<Club> clubs = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        listClubs.setItems(clubs);
        listTeams.setItems(teams);

        listClubs.setCellFactory(param -> new ModelCell<>());
        listTeams.setCellFactory(param -> new ModelCell<>());

        listClubs.getSelectionModel().selectedItemProperty().addListener((o, oVal, nVal) -> selectClub(nVal));

        choiceNationality.getItems().setAll(Nation.values());
        choiceType.getItems().setAll(ch.tiim.sco.lenex.model.Club.TypeClub.values());

        spinnerClubId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
    }

    private void selectClub(Club c) {
        if (c == null) return;
        fieldName.setText(c.getName());
        fieldShortName.setText(c.getNameShort());
        fieldEnglishName.setText(c.getNameEn());
        fieldEnglishShortName.setText(c.getNameShortEn());
        fieldClubCode.setText(c.getCode());
        if (c.getNationality() != null && !c.getNationality().isEmpty()) {
            choiceNationality.setValue(Nation.valueOf(c.getNationality()));
        } else {
            choiceNationality.setValue(null);
        }
        spinnerClubId.getEditor().setText(String.valueOf(c.getExternId()));
        updateTeams();
    }

    private void updateTeams() {
        Club c = listClubs.getSelectionModel().getSelectedItem();
        if (c != null) {
            Team t = listTeams.getSelectionModel().getSelectedItem();
            try {
                teams.setAll(db.getTblClubContent().getTeams(c));
            } catch (Exception e) {
                LOGGER.warn("Error on loading teams", e);
            }
            listTeams.getSelectionModel().select(t);
        }
    }

    @FXML
    private void onBtnAdd() {
        Club c = getClubFromFields();
        try {
            db.getTblClub().addClub(c);
        } catch (Exception e) {
            LOGGER.warn("Error on adding club", e);
        }
        updateClubs();
    }

    private Club getClubFromFields() {
        return new Club(
                fieldName.getText(),
                fieldShortName.getText(),
                fieldEnglishName.getText(),
                fieldEnglishShortName.getText(),
                fieldClubCode.getText(),
                choiceNationality.getValue() == null ? null : choiceNationality.getValue().toString(),
                spinnerClubId.getValue()
        );
    }

    private void updateClubs() {
        Club i = listClubs.getSelectionModel().getSelectedItem();
        try {
            clubs.setAll(db.getTblClub().getAll());
        } catch (Exception e) {
            LOGGER.warn("Error on loading clubs", e);
        }
        listClubs.getSelectionModel().select(i);

    }

    @FXML
    private void onBtnDelete() {
        Club c = listClubs.getSelectionModel().getSelectedItem();
        try {
            db.getTblClub().deleteClub(c);
        } catch (Exception e) {
            LOGGER.warn("Error on deleting club", e);
        }
        updateClubs();
    }

    @FXML
    private void onBtnEdit() {
        Club c = listClubs.getSelectionModel().getSelectedItem();
        Club newC = getClubFromFields();
        newC.setId(c.getId());
        try {
            db.getTblClub().updateClub(newC);
        } catch (Exception e) {
            LOGGER.warn("Error on updating club", e);
        }
        updateClubs();
    }

    @FXML
    private void onBtnTeamEdit() {
        final Club c = listClubs.getSelectionModel().getSelectedItem();
        if (c != null) {
            AddDeletePresenter<Team> p = new AddDeleteView<Team>().getController();
            p.setIncludedFactory(() -> db.getTblClubContent().getTeams(c));
            p.setExcludedFactory(() -> db.getTblClubContent().getNotTeams(c));
            p.setAdd(team -> db.getTblClubContent().addTeam(c, team));
            p.setRemove(team -> db.getTblClubContent().deleteTeam(c, team));
            p.showAndWait();
            updateTeams();
        }
    }

    @Override
    public void opened() {
        updateClubs();
    }

    @Override
    public InputStream getIcon() {
        return ClubPresenter.class.getResourceAsStream("icon2.png");
    }

    @Override
    public String getName() {
        return "Club";
    }
}
