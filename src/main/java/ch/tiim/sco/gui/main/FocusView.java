package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.FocusEvent;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FocusView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;


    @FXML
    private Parent root;
    @FXML
    private ListView<SetFocus> foci;
    @FXML
    private Label name;
    @FXML
    private Label abbr;
    @FXML
    private TextArea notes;

    @FXML
    private void initialize() {
        foci.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelect(newValue));
        populate();
    }

    private void onSelect(SetFocus focus) {
        if (focus != null) {
            name.setText(focus.getName());
            abbr.setText(focus.getAbbr());
            notes.setText(focus.getNotes());
        }
    }

    private void populate() {
        try {
            foci.setItems(FXCollections.observableArrayList(db.getTblSetFocus().getAllFoci()));
        } catch (Exception e) {
            new ExceptionAlert(LOGGER, "Can't load focus", e, eventBus).handle();
        }
    }

    @FXML
    private void onNew() {
        eventBus.post(new FocusEvent.FocusOpenEvent(null, mainStage));
    }

    @FXML
    private void onDelete() {
        SetFocus item = foci.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblSetFocus().deleteSetFocus(item);
            } catch (Exception e) {
                new ExceptionAlert(LOGGER, "Can't delete focus", e, eventBus).handle();
            }
            eventBus.post(new FocusEvent.FocusDeleteEvent(item));
        }
    }

    @FXML
    private void onEdit() {
        SetFocus item = foci.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new FocusEvent.FocusOpenEvent(item, mainStage));
        }
    }

    @Subscribe
    public void onFocus(FocusEvent event) {
        populate();
        foci.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
