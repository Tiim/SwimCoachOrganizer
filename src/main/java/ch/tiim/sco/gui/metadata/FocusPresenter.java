package ch.tiim.sco.gui.metadata;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class FocusPresenter extends Page {
    public static final String PATTERN_NAME = "[^ ].*";
    private static final Logger LOGGER = LogManager.getLogger(FocusPresenter.class.getName());
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldAbbr;
    @FXML
    private TextArea fieldNotes;
    @FXML
    private ListView<SetFocus> list;
    @FXML
    private Label labelDesc;


    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<SetFocus> foci = FXCollections.observableArrayList();

    @Inject
    private void injected() {
        updateFocusList();
    }

    private void updateFocusList() {
        int i = list.getSelectionModel().getSelectedIndex();
        try {
            foci.setAll(db.getTblSetFocus().getAllFoci());
        } catch (Exception e) {
            LOGGER.warn("Error on loading focus", e);
        }
        list.getSelectionModel().select(i);
    }

    @FXML
    private void initialize() {
        list.setItems(foci);
        list.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectedSetFocus(newValue)
        );
        labelDesc.setText("The focus of the set, like sprint, endurance or recovery.");
    }

    private void selectedSetFocus(SetFocus newValue) {
        if (newValue != null) {
            fieldName.setText(newValue.getName());
            fieldAbbr.setText(newValue.getAbbr());
            fieldNotes.setText(newValue.getNotes());
        } else {
            fieldName.setText("");
            fieldAbbr.setText("");
            fieldNotes.setText("");
        }
    }

    @FXML
    void onBtnSave() {
        if (!validate()) return;
        SetFocus f = list.getSelectionModel().getSelectedItem();
        if (f == null) {
            onBtnNew();
        } else {
            String name = fieldName.getText();
            String abbr = fieldAbbr.getText();
            String notes = fieldNotes.getText();
            try {
                db.getTblSetFocus().updateSetFocus(new SetFocus(f.getId(), name, abbr, notes));
            } catch (Exception e) {
                LOGGER.warn("Error on updating focus", e);
            }
            updateFocusList();
        }
    }

    private boolean validate() {
        if (!fieldName.getText().matches(PATTERN_NAME)) {
            fieldName.requestFocus();
            fieldName.selectAll();
            return false;
        }
        if (fieldAbbr.getText().isEmpty()) {
            fieldAbbr.setText(fieldName.getText());
        }
        return true;
    }

    @FXML
    void onBtnNew() {
        if (!validate()) return;
        String name = fieldName.getText();
        String abbr = fieldAbbr.getText();
        String notes = fieldNotes.getText();
        try {
            db.getTblSetFocus().addSetFocus(new SetFocus(name, abbr, notes));
        } catch (Exception e) {
            LOGGER.warn("Error on adding focus", e);
        }
        updateFocusList();
    }

    @FXML
    void onBtnDelete() {
        SetFocus f = list.getSelectionModel().getSelectedItem();
        if (f != null) {
            try {
                db.getTblSetFocus().deleteSetFocus(f);
            } catch (Exception e) {
                LOGGER.warn("Error on deleting focus", e);
            }
            updateFocusList();
        }
    }

    @Override
    public void opened() {

    }

    @Override
    public InputStream getIcon() {
        return FocusPresenter.class.getResourceAsStream("focus.png");
    }

    @Override
    public String getName() {
        return "Set Focus";
    }
}
