package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.OpenEvent;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.util.DurationFormatter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.Duration;

public class SetDialog extends DialogView {

    private Set currentSet;

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
    private TextField time;
    @FXML
    private RadioButton isIntervall;
    @FXML
    private TextArea notes;


    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private void initialize() {
        distance1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));
        distance2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));
        distance3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 100, 25));
        try {
            focus.setItems(FXCollections.observableArrayList(db.getTblSetFocus().getAllFoci()));
            stroke.setItems(FXCollections.observableArrayList(db.getTblSetStroke().getAllStrokes()));
        } catch (Exception e) {
            new ExceptionAlert(LOGGER, "Can't query database", e, eventBus).handle();
        }
    }

    @FXML
    private void onSave() {

        boolean update = true;
        if (currentSet == null) {
            currentSet = new Set();
            update = false;
        }

        currentSet.setName(name.getText());
        currentSet.setContent(content.getText());
        currentSet.setDistance1(distance1.getValue());
        currentSet.setDistance2(distance2.getValue());
        currentSet.setDistance3(distance3.getValue());
        currentSet.setIntensity((int) intensity.getValue());
        currentSet.setFocus(focus.getValue());
        currentSet.setStroke(stroke.getValue());
        currentSet.setInterval((int) DurationFormatter.parse(time.getText()).toMillis());
        currentSet.setIsPause(!isIntervall.isSelected());
        currentSet.setNotes(notes.getText());

        try {
            if (update) {
                db.getTblSet().updateSet(currentSet);
            } else {
                db.getTblSet().addSet(currentSet);
            }
        } catch (Exception e) {
            LOGGER.warn("Can't save set");
        }

        eventBus.post(new SetEvent.SetSaveEvent(currentSet));
        close();
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
            time.setText(DurationFormatter.format(Duration.ofMinutes(1)));
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
            time.setText(DurationFormatter.format(Duration.ofMillis(set.getInterval())));
            isIntervall.setSelected(!set.isPause());
            notes.setText(set.getNotes());
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
