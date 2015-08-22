package ch.tiim.sco.gui.member;

import ch.tiim.inject.Inject;
import ch.tiim.javafx.ValidationListener;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.TeamMember;
import ch.tiim.sco.gui.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class MemberPresenter extends Page {
    private static final String PATTERN_NAME = "^[^ ].*[^ ]$";
    private static final String PATTERN_EMAIL = "^.+@.+\\..+$|^$";
    private static final String PATTERN_PHONE = "^.*(?=.*\\d).*$|^$";
    private static final Logger LOGGER = LogManager.getLogger(MemberPresenter.class.getName());
    private final ObservableList<TeamMember> members = FXCollections.observableArrayList();
    @FXML
    private ListView<TeamMember> listMembers;
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
            members.setAll(db.getTblTeamMember().getAllMembers());
            listMembers.getSelectionModel().select(i);
    }

    @FXML
    private void initialize() {
        listMembers.setItems(members);
        listMembers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selected(newValue)
        );
        fieldNameFirst.textProperty().addListener(new ValidationListener(PATTERN_NAME, fieldNameFirst));
        fieldNameLast.textProperty().addListener(new ValidationListener(PATTERN_NAME, fieldNameLast));
        fieldEmail.textProperty().addListener(new ValidationListener(PATTERN_EMAIL, fieldEmail));
        fieldPhonePriv.textProperty().addListener(new ValidationListener(PATTERN_PHONE, fieldPhonePriv));
        fieldPhoneMob.textProperty().addListener(new ValidationListener(PATTERN_PHONE, fieldPhoneMob));
        fieldPhoneWork.textProperty().addListener(new ValidationListener(PATTERN_PHONE, fieldPhoneWork));
    }

    private void selected(TeamMember v) {
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
                db.getTblTeamMember().addMember(getMember());
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

    private TeamMember getMember() {
        return new TeamMember(
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
        TeamMember m = listMembers.getSelectionModel().getSelectedItem();
        if (validate() && m != null) {
            TeamMember newM = getMember();
            newM.setId(m.getId());
                db.getTblTeamMember().updateMember(newM);
                updateMemberList();
        }
    }

    @FXML
    private void onBtnDelete() {
        TeamMember m = listMembers.getSelectionModel().getSelectedItem();
        if (m != null) {
                db.getTblTeamMember().deleteMember(m);
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
}
