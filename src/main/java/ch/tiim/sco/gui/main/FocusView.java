package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.events.focus.FocusDeleteEvent;
import ch.tiim.sco.gui.events.focus.FocusEvent;
import ch.tiim.sco.gui.events.focus.FocusOpenEvent;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class FocusView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;


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
        reload();
    }

    private void onSelect(SetFocus focus) {
        if (focus != null) {
            name.setText(focus.getName());
            abbr.setText(focus.getAbbr());
            notes.setText(focus.getName());
        }
    }

    private void reload() {
        try {
            foci.setItems(FXCollections.observableArrayList(db.getTblSetFocus().getAllFoci()));
        } catch (Exception e) {
            LOGGER.warn("Can't load focus", e);
        }
    }

    @FXML
    private void onNew() {
        eventBus.post(new FocusOpenEvent(null));
    }

    @FXML
    private void onDelete() {
        SetFocus item = foci.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblSetFocus().deleteSetFocus(item);
            } catch (Exception e) {
                LOGGER.warn("Can't delete focus", e);
            }
            eventBus.post(new FocusDeleteEvent(item));
        }
    }

    @FXML
    private void onEdit() {
        SetFocus item = foci.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new FocusOpenEvent(item));
        }
    }

    @Subscribe
    public void onFocus(FocusEvent event) {
        reload();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
