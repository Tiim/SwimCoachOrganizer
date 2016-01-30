package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.FocusEvent;
import ch.tiim.sco.util.OutOfCoffeeException;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FocusView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(FocusView.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

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
        initMenu();
        foci.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelect(newValue));
        isSelected.bind(foci.getSelectionModel().selectedItemProperty().isNotNull());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem("Export Focus", isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented");
                }),
                new SeparatorMenuItem(),
                createItem("New Focus", null, event -> onNew()),
                createItem("Edit Focus", isSelected, event1 -> onEdit()),
                createItem("Delete Focus", isSelected, event2 -> onDelete())
        );
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
            ExceptionAlert.showError(LOGGER, "Can't load focus", e, eventBus);
        }
    }

    private void onNew() {
        eventBus.post(new FocusEvent.FocusOpenEvent(null, mainStage));
    }

    private void onEdit() {
        SetFocus item = foci.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new FocusEvent.FocusOpenEvent(item, mainStage));
        }
    }

    private void onDelete() {
        SetFocus item = foci.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblSetFocus().deleteSetFocus(item);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, "Can't delete focus", e, eventBus);
            }
            eventBus.post(new FocusEvent.FocusDeleteEvent(item));
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
