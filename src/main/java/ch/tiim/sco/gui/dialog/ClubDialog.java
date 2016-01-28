package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.ClubEvent;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.lenex.model.Nation;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClubDialog extends DialogView {


    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private Parent root;
    @FXML
    private TextField name;
    @FXML
    private TextField nameShort;
    @FXML
    private TextField nameEn;
    @FXML
    private TextField nameShortEn;
    @FXML
    private ChoiceBox<Nation> country;
    @FXML
    private TextField code;
    @FXML
    private Spinner<Integer> externid;
    @FXML
    private ListView<Team> teams;

    private HashMap<Team, ObservableValue<Boolean>> selected = new HashMap<>();
    private Club currentClub;

    @FXML
    private void initialize() {
        Nation[] nations = Nation.values();
        Arrays.sort(nations);
        country.getItems().setAll(nations);
        externid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        teams.setCellFactory(CheckBoxListCell.forListView(param -> selected.get(param), new StringConverter<Team>() {
            @Override
            public String toString(Team object) {
                return object.uiString();
            }

            @Override
            public Team fromString(String string) {
                return null;
            }
        }));
    }

    @FXML
    void onCancel() {
        close();
    }

    @FXML
    void onSave() {
        List<Team> teams = selected.keySet().stream()
                .filter(team -> selected.get(team).getValue())
                .collect(Collectors.toList());

        boolean isNew = false;
        if (currentClub == null) {
            isNew = true;
            currentClub = new Club();
        }
        currentClub.setName(name.getText());
        currentClub.setNameShort(nameShort.getText());
        currentClub.setNameEn(nameEn.getText());
        currentClub.setNameShortEn(nameShortEn.getText());
        currentClub.setNationality(country.getValue().toString());
        currentClub.setCode(code.getText());
        currentClub.setExternId(externid.getValue());
        try {
            if (isNew) {
                db.getTblClub().addClub(currentClub);
            } else {
                db.getTblClub().updateClub(currentClub);
            }
            db.getTblClubContent().setTeams(currentClub, teams);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't save club", e, eventBus);
        }
        eventBus.post(new ClubEvent.ClubSaveEvent(currentClub));
        close();
    }

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((ClubEvent.ClubOpenEvent) event).getObj());
    }

    private void populate(Club club) {
        currentClub = club;
        if (club != null) {
            name.setText(club.getName());
            nameShort.setText(club.getNameShort());
            nameEn.setText(club.getNameEn());
            nameShortEn.setText(club.getNameShortEn());
            if (club.getNationality() != null) {
                country.setValue(Nation.valueOf(club.getNationality()));
            }
            code.setText(club.getCode());
            externid.getValueFactory().setValue(club.getExternId());
        }
        List<Team> notInClub;
        List<Team> inClub;
        try {
            if (club != null) {
                inClub = db.getTblClubContent().getTeams(club);
                notInClub = db.getTblClubContent().getNotTeams(club);
            } else {
                inClub = new ArrayList<>();
                notInClub = db.getTblTeam().getAllTeams();
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load teams", e, eventBus);
            return;
        }
        inClub.forEach(team -> selected.put(team, new SimpleBooleanProperty(true)));
        notInClub.forEach(team -> selected.put(team, new SimpleBooleanProperty(false)));
        teams.getItems().setAll(inClub);
        teams.getItems().addAll(notInClub);
    }


    @Override
    public Parent getRoot() {
        return root;
    }
}
