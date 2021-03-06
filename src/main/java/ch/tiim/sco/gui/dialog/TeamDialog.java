package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.TeamEvent;
import ch.tiim.sco.gui.util.ModelConverter;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.gui.util.Validator;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TeamDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamDialog.class);
    private final HashMap<Swimmer, ObservableValue<Boolean>> selected = new HashMap<>();
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private VBox root;
    @FXML
    private TextField name;
    @FXML
    private ListView<Swimmer> swimmers;
    private Team currentTeam;

    @FXML
    private void initialize() {
        swimmers.setCellFactory(CheckBoxListCell.forListView(selected::get, new ModelConverter<>(lang)));
    }

    @FXML
    private void onCancel() {
        close();
    }

    @FXML
    private void onSave() {
        try {
            currentTeam = getTeam();
        } catch (UIException e) {
            e.showDialog(lang.str("gui.missing"));
            return;
        }
        List<Swimmer> swimmers = selected.keySet().stream()
                .filter(swimmer -> selected.get(swimmer).getValue())
                .collect(Collectors.toList());
        try {
            if (currentTeam.getId() == null) {
                db.getTblTeam().addTeam(currentTeam);
            } else {
                db.getTblTeam().updateTeam(currentTeam);
            }
            db.getTblTeamContent().setSwimmers(currentTeam, swimmers);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.save", "error.subj.team"), e);
        }
        close();
        eventBus.post(new TeamEvent.TeamSaveEvent(currentTeam));
    }

    private Team getTeam() throws UIException {
        String name = Validator.strNotEmpty(this.name.getText(), lang.str("gui.name"));
        if (currentTeam == null) {
            currentTeam = new Team(name);
        } else {
            currentTeam.setName(name);
        }
        return currentTeam;
    }

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        populate(((TeamEvent.TeamOpenEvent) event).getObj());
    }

    private void populate(Team team) {
        this.currentTeam = team;
        List<Swimmer> inTeam;
        List<Swimmer> notInTeam;
        try {
            if (team != null) {
                name.setText(team.getName());
                inTeam = db.getTblTeamContent().getSwimmers(team);
                notInTeam = db.getTblTeamContent().getSwimmersNotInTeam(team);
            } else {
                inTeam = new ArrayList<>();
                notInTeam = db.getTblSwimmer().getAllSwimmers();
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.swimmer"), e);
            return;
        }
        inTeam.forEach(swimmer -> selected.put(swimmer, new SimpleBooleanProperty(true)));
        notInTeam.forEach(swimmer -> selected.put(swimmer, new SimpleBooleanProperty(false)));
        swimmers.getItems().setAll(inTeam);
        swimmers.getItems().addAll(notInTeam);

    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
