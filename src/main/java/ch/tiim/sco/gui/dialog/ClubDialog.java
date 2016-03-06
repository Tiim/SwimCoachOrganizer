package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.ClubEvent;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.util.ModelConverter;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.gui.util.Validator;
import ch.tiim.sco.lenex.model.Nation;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClubDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClubDialog.class);

    private final HashMap<Team, ObservableValue<Boolean>> selected = new HashMap<>();
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;
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
    private Club currentClub;

    @FXML
    private void initialize() {
        Nation[] nations = Nation.values();
        Arrays.sort(nations);
        country.getItems().setAll(nations);
        externid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        teams.setCellFactory(CheckBoxListCell.forListView(selected::get, new ModelConverter<>(lang)));
    }

    @FXML
    private void onCancel() {
        close();
    }

    @FXML
    private void onSave() {
        List<Team> teams = selected.keySet().stream()
                .filter(team -> selected.get(team).getValue())
                .collect(Collectors.toList());
        try {
            currentClub = getClub();
        } catch (UIException e) {
            e.showDialog(lang.str("gui.missing"));
            return;
        }
        try {
            if (currentClub.getId() == null) {
                db.getTblClub().addClub(currentClub);
            } else {
                db.getTblClub().updateClub(currentClub);
            }
            db.getTblClubContent().setTeams(currentClub, teams);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.save", "error.subj.club"), e);
        }
        eventBus.post(new ClubEvent.ClubSaveEvent(currentClub));
        close();
    }

    private Club getClub() throws UIException {
        if (currentClub == null) {
            currentClub = new Club();
        }
        String name = Validator.strNotEmpty(this.name.getText(), lang.str("gui.name"));
        currentClub.setName(name);
        currentClub.setNameShort(nameShort.getText());
        currentClub.setNameEn(nameEn.getText());
        currentClub.setNameShortEn(nameShortEn.getText());
        Nation value = Validator.nonNull(country.getValue(), lang.str("gui.country"));
        currentClub.setNationality(value.toString());
        currentClub.setCode(code.getText());
        currentClub.setExternId(externid.getValue());
        return currentClub;
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
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.team"), e);
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
