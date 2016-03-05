package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.event.EmailEvent;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.ClubEvent;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClubView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClubView.class);
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "host")
    private HostServices host;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

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

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initMenu();
        clubs.setCellFactory(param -> new ModelCell<>());
        clubs.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        teams.setCellFactory(param -> new ModelCell<>());
        isSelected.bind(clubs.getSelectionModel().selectedItemProperty().isNotNull());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem(lang.format("gui.export", "gui.club"), isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented yet");
                }),
                new SeparatorMenuItem(),
                createItem(lang.str("gui.email.club"), isSelected, event4 -> onEmail()),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.club"), null, event1 -> onNew()),
                createItem(lang.format("gui.edit", "gui.club"), isSelected, event2 -> onEdit()),
                createItem(lang.format("gui.delete", "gui.club"), isSelected, event3 -> onDelete())
        );
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
                ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.team"), e);
            }
        }
    }

    private void populate() {
        try {
            clubs.getItems().setAll(db.getTblClub().getAll());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.club"), e);
        }
    }

    private void onEmail() {
        try {
            Club c = clubs.getSelectionModel().getSelectedItem();
            if (c != null) {
                List<Team> teams = db.getTblClubContent().getTeams(c);
                List<Swimmer> swimmers = new ArrayList<>();
                for (Team t : teams) {
                    swimmers.addAll(db.getTblTeamContent().getSwimmers(t));
                }
                EmailEvent event = new EmailEvent(swimmers);
                event.setOnSucceeded(event1 -> host.showDocument(event.getValue()));
                eventBus.post(event);
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.swimmer"), e);
        }
    }

    private void onNew() {
        eventBus.post(new ClubEvent.ClubOpenEvent(null, mainStage));
    }

    private void onEdit() {
        Club club = clubs.getSelectionModel().getSelectedItem();
        if (club != null) {
            eventBus.post(new ClubEvent.ClubOpenEvent(club, mainStage));
        }
    }

    private void onDelete() {
        Club club = clubs.getSelectionModel().getSelectedItem();
        if (club != null) {
            try {
                db.getTblClub().deleteClub(club);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.club"), e);
            }
            eventBus.post(new ClubEvent.ClubDeleteEvent(club));
        }
    }

    @Subscribe
    public void onClub(ClubEvent event) {
        populate();
        clubs.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
