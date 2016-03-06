package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.component.DurationField;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.util.ModelConverter;
import ch.tiim.sco.gui.util.UIException;
import ch.tiim.sco.gui.util.Validator;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class SetDialog extends DialogView {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetDialog.class);
    private Set currentSet;

    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private TextField name;
    @FXML
    private TextField content;
    @FXML
    private Spinner<Integer> distance1;
    @FXML
    private Spinner<Integer> distance2;
    @FXML
    private Spinner<Integer> distance3;
    @FXML
    private Slider intensity;
    @FXML
    private ChoiceBox<SetFocus> focus;
    @FXML
    private ChoiceBox<SetStroke> stroke;
    @FXML
    private DurationField time;
    @FXML
    private RadioButton isIntervall;
    @FXML
    private TextArea notes;


    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private void initialize() {
        focus.setConverter(new ModelConverter<>(lang));
        stroke.setConverter(new ModelConverter<>(lang));
        distance1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));
        distance2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));
        distance3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 100, 25));
        try {
            focus.setItems(FXCollections.observableArrayList(db.getTblSetFocus().getAllFoci()));
            stroke.setItems(FXCollections.observableArrayList(db.getTblSetStroke().getAllStrokes()));
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.database"), e);
        }
    }

    @FXML
    private void onSave() {
        try {
            currentSet = getSet();
        } catch (UIException e) {
            e.showDialog(lang.str("gui.missing"));
            return;
        }

        try {
            if (currentSet.getId() != null) {
                db.getTblSet().updateSet(currentSet);
            } else {
                db.getTblSet().addSet(currentSet);
            }
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.save", "error.subj.set"), e);
        }

        eventBus.post(new SetEvent.SetSaveEvent(currentSet));
        close();
    }

    private Set getSet() throws UIException {
        if (currentSet == null) {
            currentSet = new Set();
        }
        String name = Validator.strNotEmpty(this.name.getText(), lang.str("gui.name"));
        currentSet.setName(name);
        String content = Validator.strNotEmpty(this.content.getText(), lang.str("gui.content"));
        currentSet.setContent(content);
        currentSet.setDistance1(distance1.getValue());
        currentSet.setDistance2(distance2.getValue());
        currentSet.setDistance3(distance3.getValue());
        currentSet.setIntensity((int) intensity.getValue());
        currentSet.setFocus(focus.getValue());
        currentSet.setStroke(stroke.getValue());
        currentSet.setInterval((int) time.getDuration().toMillis());
        currentSet.setIsPause(!isIntervall.isSelected());
        currentSet.setNotes(notes.getText());
        return currentSet;
    }

    @FXML
    private void onCancel() {
        close();
    }

    @Override
    public void open(OpenEvent e, Stage parent) {
        super.open(e, parent);
        populate(((SetEvent.SetOpenEvent) e).getObj());
    }

    private void populate(Set set) {
        currentSet = set;
        if (set == null) {
            name.setText("");
            content.setText("");
            distance1.getValueFactory().setValue(1);
            distance2.getValueFactory().setValue(1);
            distance3.getValueFactory().setValue(100);
            intensity.setValue(.5f);
            focus.getSelectionModel().select(0);
            stroke.getSelectionModel().select(0);
            time.setDuration(Duration.ofMinutes(1));
            isIntervall.setSelected(true);
            notes.setText("");
        } else {
            name.setText(set.getName());
            content.setText(set.getContent());
            distance1.getValueFactory().setValue(set.getDistance1());
            distance2.getValueFactory().setValue(set.getDistance2());
            distance3.getValueFactory().setValue(set.getDistance3());
            intensity.setValue(set.getIntensity());
            focus.getSelectionModel().select(set.getFocus());
            stroke.getSelectionModel().select(set.getStroke());
            time.setDuration(Duration.ofMillis(set.getInterval()));
            isIntervall.setSelected(!set.isPause());
            notes.setText(set.getNotes());
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
