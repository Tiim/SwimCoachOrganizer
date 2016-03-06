package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.event.EmailEvent;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.SwimmerEvent;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class SwimmerView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwimmerView.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "host")
    private HostServices host;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private SplitPane root;
    @FXML
    private ListView<Swimmer> swimmers;
    @FXML
    private TextField firstName;
    @FXML
    private Hyperlink email;
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

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initMenu();
        swimmers.setCellFactory(param -> new ModelCell<>());
        swimmers.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        isSelected.bind(swimmers.getSelectionModel().selectedItemProperty().isNotNull());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem(lang.format("gui.export", "gui.swimmer"), isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented yet");
                }),
                new SeparatorMenuItem(),
                createItem(lang.str("gui.email.swimmer"), isSelected, event1 -> onEmail()),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.swimmer"), null, event2 -> onNew()),
                createItem(lang.format("gui.edit", "gui.swimmer"), isSelected, event3 -> onEdit()),
                createItem(lang.format("gui.delete", "gui.swimmer"), isSelected, event4 -> onDelete())
        );
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
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.swimmer"), e);
        }
    }

    @FXML
    private void onEmail() {
        Swimmer sw = swimmers.getSelectionModel().getSelectedItem();
        if (sw != null) {
            EmailEvent event = new EmailEvent(Collections.singletonList(sw), lang);
            event.setOnSucceeded(event1 -> host.showDocument(event.getValue()));
            eventBus.post(event);
        }
    }

    private void onNew() {
        eventBus.post(new SwimmerEvent.SwimmerOpenEvent(null, mainStage));
    }

    private void onEdit() {
        Swimmer sw = swimmers.getSelectionModel().getSelectedItem();
        if (sw != null) {
            eventBus.post(new SwimmerEvent.SwimmerOpenEvent(sw, mainStage));
        }
    }

    private void onDelete() {
        Swimmer sw = swimmers.getSelectionModel().getSelectedItem();
        if (sw != null) {
            try {
                db.getTblSwimmer().deleteSwimmer(sw);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.swimmer"), e);
            }
            eventBus.post(new SwimmerEvent.SwimmerDeleteEvent(sw));
        }
    }

    @Subscribe
    public void onSwimmer(SwimmerEvent event) {
        populate();
        swimmers.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
