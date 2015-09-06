package ch.tiim.sco.gui.sets;

import ch.tiim.inject.Inject;
import ch.tiim.javafx.ValidationListener;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.database.model.SetForm;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.utils.ModelCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SetsPresenter extends Page {

    private static final String PATTERN_NAME = "[^ ].*";
    private static final String PATTERN_NUMBER = "\\d+";
    private static final Logger LOGGER = LogManager.getLogger(SetsPresenter.class.getName());
    private final ObservableList<Set> sets = FXCollections.observableArrayList();
    private final ObservableList<SetFocus> foci = FXCollections.observableArrayList();
    private final ObservableList<SetForm> forms = FXCollections.observableArrayList();
    @FXML
    private ListView<Set> listSets;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldDistance1;
    @FXML
    private TextField fieldDistance2;
    @FXML
    private TextField fieldDistance3;
    @FXML
    private Slider sliderIntensity;
    @FXML
    private ChoiceBox<SetFocus> choiceFocus;
    @FXML
    private ChoiceBox<SetForm> choiceForm;
    @FXML
    private TextField fieldPause;
    @FXML
    private TextField fieldContent;
    @FXML
    private TextArea areaNotes;
    @FXML
    private Label intensityVal;
    @FXML
    private RadioButton radioPause;
    @FXML
    private RadioButton radioInterval;
    @Inject(name = "db-controller")
    private DatabaseController db;

    @Override
    public void opened() {
        try {
            foci.setAll(db.getTblSetFocus().getAllFoci());
            foci.add(0, new SetFocus(0, "Nothing", "-", ""));
            forms.setAll(db.getTblSetForm().getAllForms());
            forms.add(0, new SetForm(0, "Nothing", "-", ""));
        } catch (Exception e) {
            LOGGER.warn("Error on loading focus or form", e);
        }
    }

    @Override
    public String getName() {
        return "Set";
    }

    @Inject
    private void injected() {
        updateSetList();
    }

    private void updateSetList() {
        int i = listSets.getSelectionModel().getSelectedIndex();
        try {
            sets.setAll(db.getTblSet().getAllSets());
        } catch (Exception e) {
            LOGGER.warn("Error on loading sets", e);
        }
        listSets.getSelectionModel().select(i);
    }

    @FXML
    private void initialize() {
        listSets.itemsProperty().setValue(sets);
        listSets.setCellFactory(param -> new ModelCell<>());
        listSets.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            selectedNewSet(newVal);
        });

        intensityVal.textProperty().bind(sliderIntensity.valueProperty().asString("%.0f"));
        fieldName.textProperty().addListener(new ValidationListener(PATTERN_NAME, fieldName));
        fieldDistance1.textProperty().addListener(new ValidationListener(PATTERN_NUMBER, fieldDistance1));
        fieldDistance2.textProperty().addListener(new ValidationListener(PATTERN_NUMBER, fieldDistance2));
        fieldDistance3.textProperty().addListener(new ValidationListener(PATTERN_NUMBER, fieldDistance3));
        fieldPause.textProperty().addListener(new ValidationListener(PATTERN_NUMBER, fieldPause));
        choiceFocus.itemsProperty().setValue(foci);
        choiceForm.itemsProperty().setValue(forms);
    }

    private void selectedNewSet(Set newVal) {
        if (newVal != null) {
            fieldName.setText(newVal.getName());
            fieldDistance1.setText(Integer.toString(newVal.getDistance1()));
            fieldDistance2.setText(Integer.toString(newVal.getDistance2()));
            fieldDistance3.setText(Integer.toString(newVal.getDistance3()));
            sliderIntensity.setValue(((double) newVal.getIntensity()));
            fieldPause.setText(Integer.toString(newVal.getInterval()));
            if (newVal.getFocus() == null) {
                choiceFocus.getSelectionModel().select(0);
            } else {
                choiceFocus.getSelectionModel().select(newVal.getFocus());
            }
            if (newVal.getForm() == null) {
                choiceForm.getSelectionModel().select(0);
            } else {
                choiceForm.getSelectionModel().select(newVal.getForm());
            }
            fieldContent.setText(newVal.getContent());
            areaNotes.setText(newVal.getNotes());
            radioInterval.setSelected(!newVal.isPause());
            radioPause.setSelected(newVal.isPause());
        } else {
            fieldName.setText("");
            fieldDistance1.setText("");
            fieldDistance2.setText("");
            fieldDistance3.setText("");
            sliderIntensity.setValue(0);
            fieldPause.setText("");
            choiceFocus.getSelectionModel().select(0);
            choiceForm.getSelectionModel().select(0);
            fieldContent.setText("");
            areaNotes.setText("");
            radioInterval.setSelected(false);
            radioPause.setSelected(true);
        }
    }

    @FXML
    private void onBtnSave() {
        Set set = listSets.getSelectionModel().getSelectedItem();
        if (set == null) {
            onBtnNew();
        } else {
            if (!validate()) return;
            Set newSet = getSetFromFields();
            newSet.setId(set.getId());
            try {
                db.getTblSet().updateSet(newSet);
            } catch (Exception e) {
                LOGGER.warn("Error on updating set", e);
            }
            updateSetList();
        }
    }

    @FXML
    private void onBtnNew() {
        LOGGER.info("new set created");
        if (!validate()) return;
        Set set = getSetFromFields();
        try {
            db.getTblSet().addSet(set);
        } catch (Exception e) {
            LOGGER.warn("Error on adding set", e);
        }
        updateSetList();
    }

    private boolean validate() {
        List<TextField> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(fieldDistance1, fieldDistance2, fieldDistance3, fieldPause));
        if (!fieldName.getText().matches(PATTERN_NAME)) {
            fieldName.requestFocus();
            fieldName.selectAll();
            return false;
        }
        for (TextField f : fields) {
            if (!f.getText().matches(PATTERN_NUMBER)) {
                f.requestFocus();
                f.selectAll();
                return false;
            }
        }
        return true;
    }

    private Set getSetFromFields() {
        return new Set(
                fieldName.getText(),
                fieldContent.getText(),
                Integer.parseInt(fieldDistance1.getText()),
                Integer.parseInt(fieldDistance2.getText()),
                Integer.parseInt(fieldDistance3.getText()),
                (int) sliderIntensity.getValue(),
                choiceFocus.getValue(),
                choiceForm.getValue(),
                areaNotes.getText(),
                Integer.parseInt(fieldPause.getText()),
                radioPause.isSelected()
        );
    }

    @FXML
    private void onBtnDelete() {
        LOGGER.info("Delete set");
        try {
            db.getTblSet().deleteSet(listSets.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            LOGGER.warn("Error on deleting set", e);
        }
        updateSetList();
    }
}
