package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetStroke;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.StrokeEvent;
import ch.tiim.sco.gui.util.ExportUtil;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrokeView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrokeView.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private TextField search;
    @FXML
    private ListView<SetStroke> strokes;
    @FXML
    private Label name;
    @FXML
    private Label abbr;
    @FXML
    private TextArea notes;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private FilteredList<SetStroke> allStrokes;

    @FXML
    private void initialize() {
        initMenu();
        strokes.setCellFactory(param -> new ModelCell<>(lang));
        strokes.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelect(newValue));
        isSelected.bind(strokes.getSelectionModel().selectedItemProperty().isNotNull());
        search.textProperty().addListener(observable -> {
            if (search.getText() == null || search.getText().isEmpty()) {
                allStrokes.setPredicate(set -> true);
            } else {
                allStrokes.setPredicate(set -> set.uiString(lang).toLowerCase().contains(search.getText().toLowerCase()));
            }
        });
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem(lang.format("gui.export", "gui.stroke"), isSelected, event -> onExport()),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.stroke"), null, event1 -> onNew()),
                createItem(lang.format("gui.edit", "gui.stroke"), isSelected, event2 -> onEdit()),
                createItem(lang.format("gui.delete", "gui.stroke"), isSelected, event3 -> onDelete())
        );
    }

    private void onExport() {
        new ExportUtil(db).export(strokes.getSelectionModel().getSelectedItem(), mainStage, lang);
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
            allStrokes = new FilteredList<>(FXCollections.observableArrayList(db.getTblSetStroke().getAllStrokes()));
            strokes.setItems(allStrokes);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.stroke"), e);
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
                ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.stroke"), e);
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
