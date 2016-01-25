package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.SwimmerEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SwimmerDialog extends DialogView {

    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private GridPane root;
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
    private DatePicker birthDay;
    @FXML
    private TextArea notes;

    private Swimmer currentSwimmer;

    @FXML
    private void onCancel(ActionEvent event) {
        close();
    }

    @FXML
    private void onSave(ActionEvent event) {
        boolean isNew = false;
        if (currentSwimmer == null) {
            isNew = true;
            currentSwimmer = new Swimmer();
        }
        currentSwimmer.setFirstName(firstName.getText());
        currentSwimmer.setEmail(email.getText());
        currentSwimmer.setLastName(lastName.getText());
        currentSwimmer.setPhonePrivate(phonePrivate.getText());
        currentSwimmer.setPhoneMobile(phoneMobile.getText());
        currentSwimmer.setPhoneWork(phoneWork.getText());
        currentSwimmer.setAddress(address1.getText() + "\n" + address2.getText() + "\n" + address3.getText());
        if (licenseId.getText() != null && !licenseId.getText().isEmpty()) {
            currentSwimmer.setId(Integer.parseInt(licenseId.getText()));
        }
        currentSwimmer.setIsFemale(isFemale.isSelected());
        currentSwimmer.setBirthDay(birthDay.getValue());
        currentSwimmer.setNotes(notes.getText());
        try {
            if (isNew) {
                db.getTblSwimmer().addSwimmer(currentSwimmer);
            } else {
                db.getTblSwimmer().updateSwimmer(currentSwimmer);
            }
        } catch (Exception e) {
            LOGGER.warn("Can't save swimmer", e);
        }
        eventBus.post(new SwimmerEvent.SwimmerSaveEvent(currentSwimmer));
        close();
    }

    @Override
    public void open(OpenEvent event, Stage parent) {
        super.open(event, parent);
        pupulate(((SwimmerEvent.SwimmerOpenEvent) event).getObj());
    }

    private void pupulate(Swimmer swimmer) {
        currentSwimmer = swimmer;
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
            birthDay.setValue(swimmer.getBirthDay());
            notes.setText(swimmer.getNotes());
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
