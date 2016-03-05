package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.SetEvent;
import ch.tiim.sco.gui.util.ModelCell;
import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetView.class);
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private ListView<Set> sets;

    @FXML
    private Label name;
    @FXML
    private Label intensity;
    @FXML
    private Label distance;
    @FXML
    private Label time;
    @FXML
    private Label focus;
    @FXML
    private Label stroke;
    @FXML
    private TextField content;
    @FXML
    private TextArea notes;


    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initMenu();
        sets.setCellFactory(param -> new ModelCell<>());
        sets.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelected(newValue));
        isSelected.bind(sets.getSelectionModel().selectedItemProperty().isNotNull());
        populate();
    }

    private void initMenu() {
        getMenu().getItems().setAll(
                createItem(lang.format("gui.export", "gui.set"), isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented");
                }),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.set"), null, event1 -> onNew()),
                createItem(lang.format("gui.edit", "gui.set"), isSelected, event2 -> onEdit()),
                createItem(lang.format("gui.delete", "gui.set"), isSelected, event3 -> onDelete())
        );
    }

    private void onSelected(Set set) {
        if (set != null) {
            name.setText(set.getName());
            intensity.setText(String.valueOf(set.getIntensity()));
            distance.setText(set.getDistance());
            time.setText(set.getIntervalString());
            focus.setText(set.getFocus().uiString());
            stroke.setText(set.getStroke().uiString());
            content.setText(set.getContent());
            notes.setText(set.getNotes());
        }
    }

    private void populate() {
        try {
            sets.getItems().setAll(db.getTblSet().getAllSets());
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.set"), e);
        }
    }

    private void onNew() {
        eventBus.post(new SetEvent.SetOpenEvent(null, mainStage));
    }

    private void onEdit() {
        Set set = sets.getSelectionModel().getSelectedItem();
        if (set != null) {
            eventBus.post(new SetEvent.SetOpenEvent(set, mainStage));
        }
    }

    private void onDelete() {
        Set set = sets.getSelectionModel().getSelectedItem();
        if (set != null) {
            try {
                db.getTblSet().deleteSet(set);
            } catch (Exception e) {
                ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.set"), e);
            }
            eventBus.post(new SetEvent.SetDeleteEvent(set));
        }
    }

    @Subscribe
    public void onSet(SetEvent event) {
        populate();
        sets.getSelectionModel().select(event.getObj());
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
