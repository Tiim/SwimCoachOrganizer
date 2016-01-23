package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.events.SwimmerEvent;
import ch.tiim.sco.gui.util.ModelCell;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SwimmerView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;

    @FXML
    private SplitPane root;
    @FXML
    private ListView<Swimmer> swimmers;
    @FXML
    private TextField firstName;
    @FXML
    private TextField email;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phonePrivate;
    @FXML
    private TextField phoneMobile;
    @FXML
    private TextField phoneWork;
    @FXML
    private TextField address1;
    @FXML
    private TextField address2;
    @FXML
    private TextField address3;
    @FXML
    private TextField licenseId;
    @FXML
    private RadioButton isFemale;
    @FXML
    private RadioButton isMale;
    @FXML
    private TextField birthDay;
    @FXML
    private TextArea notes;

    @FXML
    private void initialize() {
        swimmers.setCellFactory(param -> new ModelCell<>());
        swimmers.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        populate();
    }

    private void selected(Swimmer swimmer) {
        if (swimmer != null) {
            firstName.setText(swimmer.getFirstName());
            email.setText(swimmer.getEmail());
            lastName.setText(swimmer.getLastName());
            phonePrivate.setText(swimmer.getPhonePrivate());
            phoneMobile.setText(swimmer.getPhoneMobile());
            phoneWork.setText(swimmer.getPhoneWork());

            String[] address = swimmer.getAddress() == null ?
                    new String[]{} : swimmer.getAddress().split("\n");
            if (address.length == 3) {
                address1.setText(address[0]);
                address2.setText(address[1]);
                address3.setText(address[2]);
            } else {
                address1.setText("");
                address2.setText("");
                address3.setText("");
            }
            licenseId.setText(swimmer.getLicense());
            isFemale.setSelected(swimmer.isFemale());
            isMale.setSelected(!swimmer.isFemale());
            birthDay.setText(swimmer.getBirthDay().toString());
            notes.setText(swimmer.getNotes());
        }
    }

    private void populate() {
        try {
            swimmers.getItems().setAll(db.getTblSwimmer().getAllSwimmers());
        } catch (Exception e) {
            LOGGER.warn("Can't load swimmers", e);
        }
    }

    @FXML
    private void onDelete() {
        Swimmer sw = swimmers.getSelectionModel().getSelectedItem();
        if (sw != null) {
            try {
                db.getTblSwimmer().deleteSwimmer(sw);
            } catch (Exception e) {
                LOGGER.warn("Can't delete swimmer", e);
            }
            eventBus.post(new SwimmerEvent.SwimmerDeleteEvent(sw));
        }
    }

    @FXML
    private void onEdit() {
        Swimmer sw = swimmers.getSelectionModel().getSelectedItem();
        if (sw != null) {
            eventBus.post(new SwimmerEvent.SwimmerOpenEvent(sw, mainStage));
        }
    }

    @FXML
    private void onNew() {
        eventBus.post(new SwimmerEvent.SwimmerOpenEvent(null, mainStage));
    }

    @Subscribe
    public void onSwimmer(SwimmerEvent event) {
        populate();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
