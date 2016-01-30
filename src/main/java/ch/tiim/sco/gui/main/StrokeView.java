package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.StrokeEvent;
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

public class StrokeView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrokeView.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;

    @FXML
    private Parent root;
    @FXML
    private ListView<SetStroke> strokes;
    @FXML
    private Label name;
    @FXML
    private Label abbr;
    @FXML
    private TextArea notes;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initMenu();
        strokes.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelect(newValue));
        isSelected.bind(strokes.getSelectionModel().selectedItemProperty().isNotNull());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem("Export Stroke", isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented Yet");
                }),
                new SeparatorMenuItem(),
                createItem("New Stroke", null, event1 -> onNew()),
                createItem("Edit Stroke", isSelected, event2 -> onEdit()),
                createItem("Delete Stroke", isSelected, event3 -> onDelete())
        );
    }

    private void onSelect(SetStroke stroke) {
        if (stroke != null) {
            name.setText(stroke.getName());
            abbr.setText(stroke.getAbbr());
            notes.setText(stroke.getNotes());
        }
    }

    private void populate() {
        try {
            strokes.setItems(FXCollections.observableArrayList(db.getTblSetStroke().getAllStrokes()));
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, "Can't load strokes", e, eventBus);
        }
    }

    private void onNew() {
        eventBus.post(new StrokeEvent.StrokeOpenEvent(null, mainStage));
    }

    private void onEdit() {
        SetStroke item = strokes.getSelectionModel().getSelectedItem();
        if (item != null) {
            eventBus.post(new StrokeEvent.StrokeOpenEvent(item, mainStage));
        }
    }

    private void onDelete() {
        SetStroke item = strokes.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                db.getTblSetStroke().deleteSetStroke(item);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, "Can't delete focus", e, eventBus);
            }
            eventBus.post(new StrokeEvent.StrokeDeleteEvent(item));
        }
    }

    @Subscribe
    public void onStroke(StrokeEvent event) {
        populate();
        strokes.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
