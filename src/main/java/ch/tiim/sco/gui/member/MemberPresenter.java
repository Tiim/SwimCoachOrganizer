package ch.tiim.sco.gui.member;

import ch.tiim.inject.Inject;
import ch.tiim.javafx.ValidationListener;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemberPresenter extends Page {
    private static final String PATTERN_NAME = "^[^ ].*[^ ]$";
    private static final String PATTERN_EMAIL = "^.+@.+\\..+$|^$";
    private static final String PATTERN_PHONE = "^.*(?=.*\\d).*$|^$";
    private static final Logger LOGGER = LogManager.getLogger(MemberPresenter.class.getName());
    private final ObservableList<Swimmer> members = FXCollections.observableArrayList();
    @FXML
    private ListView<Swimmer> listMembers;
    @FXML
    private TextField fieldNameFirst;
    @FXML
    private TextField fieldNameLast;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldAddress1;
    @FXML
    private TextField fieldAddress2;
    @FXML
    private TextField fieldAddress3;
    @FXML
    private DatePicker fieldBirthday;
    @FXML
    private TextField fieldPhonePriv;
    @FXML
    private TextField fieldPhoneMob;
    @FXML
    private TextField fieldPhoneWork;
    @FXML
    private TextArea fieldNotes;
    @FXML
    private TextField fieldLicense;
    @FXML
    private RadioButton toggleFemale;
    @FXML
    private RadioButton toggleMale;
    @Inject(name = "db-controller")
    private DatabaseController db;

    @Inject
    private void injected() {
        updateMemberList();
    }

    private void updateMemberList() {
        int i = listMembers.getSelectionModel().getSelectedIndex();
        try {
            members.setAll(db.getTblSwimmer().getAllSwimmers());
        } catch (Exception e) {
            LOGGER.warn(e);
        }
        listMembers.getSelectionModel().select(i);
    }

    @FXML
    private void initialize() {
        listMembers.setItems(members);
        listMembers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selected(newValue)
        );
        listMembers.setCellFactory(param -> new SwimmerCell());
        fieldNameFirst.textProperty().addListener(new ValidationListener(PATTERN_NAME, fieldNameFirst));
        fieldNameLast.textProperty().addListener(new ValidationListener(PATTERN_NAME, fieldNameLast));
        fieldEmail.textProperty().addListener(new ValidationListener(PATTERN_EMAIL, fieldEmail));
        fieldPhonePriv.textProperty().addListener(new ValidationListener(PATTERN_PHONE, fieldPhonePriv));
        fieldPhoneMob.textProperty().addListener(new ValidationListener(PATTERN_PHONE, fieldPhoneMob));
        fieldPhoneWork.textProperty().addListener(new ValidationListener(PATTERN_PHONE, fieldPhoneWork));
    }

    private void selected(Swimmer v) {
        if (v == null) return;
        fieldNameFirst.setText(v.getFirstName());
        fieldNameLast.setText(v.getLastName());
        fieldEmail.setText(v.getEmail());
        String[] address = v.getAddress().split("\n");
        if (address.length == 3) {
            fieldAddress1.setText(address[0]);
            fieldAddress2.setText(address[1]);
            fieldAddress3.setText(address[2]);
        } else {
            fieldAddress1.setText("");
            fieldAddress2.setText("");
            fieldAddress3.setText("");
        }
        fieldBirthday.setValue(v.getBirthDay());
        fieldPhonePriv.setText(v.getPhonePrivate());
        fieldPhoneMob.setText(v.getPhoneMobile());
        fieldPhoneWork.setText(v.getPhoneWork());
        fieldNotes.setText(v.getNotes());
        fieldLicense.setText(v.getLicense());
        toggleFemale.setSelected(v.isFemale());
        toggleMale.setSelected(!v.isFemale());
    }

    @FXML
    private void onBtnNew() {
        if (validate()) {
            try {
                db.getTblSwimmer().addSwimmer(getMember());
            } catch (Exception e) {
                LOGGER.warn(e);
            }
            updateMemberList();
        }
    }

    private boolean validate() {
        return Page.validateTextField(fieldNameFirst, PATTERN_NAME) &&
                Page.validateTextField(fieldNameLast, PATTERN_NAME) &&
                Page.validateTextField(fieldBirthday.getEditor(), ".+") &&
                Page.validateTextField(fieldPhoneMob, PATTERN_PHONE) &&
                Page.validateTextField(fieldPhoneWork, PATTERN_PHONE) &&
                Page.validateTextField(fieldPhonePriv, PATTERN_PHONE) &&
                Page.validateTextField(fieldEmail, PATTERN_EMAIL);
    }

    private Swimmer getMember() {
        return new Swimmer(
                fieldNameFirst.getText(),
                fieldNameLast.getText(),
                fieldBirthday.getValue(),
                fieldAddress1.getText() + "\n" +
                        fieldAddress2.getText() + "\n" +
                        fieldAddress3.getText(),
                fieldPhonePriv.getText(),
                fieldPhoneWork.getText(),
                fieldPhoneMob.getText(),
                fieldEmail.getText(),
                fieldLicense.getText(),
                toggleFemale.isSelected(),
                fieldNotes.getText()
        );
    }

    @FXML
    private void onBtnEdit() {
        Swimmer m = listMembers.getSelectionModel().getSelectedItem();
        if (validate() && m != null) {
            Swimmer newM = getMember();
            newM.setId(m.getId());
            try {
                db.getTblSwimmer().updateSwimmer(newM);
            } catch (Exception e) {
                LOGGER.warn(e);
            }
            updateMemberList();
        }
    }

    @FXML
    private void onBtnDelete() {
        Swimmer m = listMembers.getSelectionModel().getSelectedItem();
        if (m != null) {
            try {
                db.getTblSwimmer().deleteSwimmer(m);
            } catch (Exception e) {
                LOGGER.warn(e);
            }
            updateMemberList();
        }
    }

    @Override
    public void opened() {
        updateMemberList();
    }

    @Override
    public String getName() {
        return "Member";
    }

    private class SwimmerCell extends ListCell<Swimmer> {
        @Override
        protected void updateItem(Swimmer item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.uiString());
            }
        }
    }
}
