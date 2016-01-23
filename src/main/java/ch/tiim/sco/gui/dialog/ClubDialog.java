package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.gui.events.ClubEvent;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.lenex.model.Nation;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;

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


    private Club currentClub;

    @FXML
    private void initialize() {
        Nation[] nations = Nation.values();
        Arrays.sort(nations);
        country.getItems().setAll(nations);
        externid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    }

    @FXML
    void onCancel() {
        close();
    }

    @FXML
    void onSave() {
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
        } catch (Exception e) {
            LOGGER.warn("Can't save club", e);
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
    }


    @Override
    public Parent getRoot() {
        return root;
    }
}
