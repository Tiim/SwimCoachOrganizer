package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.FocusEvent;
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

public class FocusView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(FocusView.class);
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
    private ListView<SetFocus> foci;
    @FXML
    private Label name;
    @FXML
    private Label abbr;
    @FXML
    private TextArea notes;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private FilteredList<SetFocus> allFoci;

    @FXML
    private void initialize() {
        initMenu();
        foci.setCellFactory(param -> new ModelCell<>(lang));
        foci.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelect(newValue));
        isSelected.bind(foci.getSelectionModel().selectedItemProperty().isNotNull());
        search.textProperty().addListener(observable -> {
            if (search.getText() == null || search.getText().isEmpty()) {
                allFoci.setPredicate(set -> true);
            } else {
                allFoci.setPredicate(set -> set.uiString(lang).toLowerCase().contains(search.getText().toLowerCase()));
            }
        });
        populate();
    }

    private void initMenu() {
        getMenu().getItems().addAll(
                createItem(lang.format("gui.export", "gui.focus"), isSelected, event -> {
                    throw new OutOfCoffeeException("Not implemented");
                }),
                new SeparatorMenuItem(),
                createItem(lang.format("gui.new", "gui.focus"), null, event -> onNew()),
                createItem(lang.format("gui.edit", "gui.focus"), isSelected, event1 -> onEdit()),
                createItem(lang.format("gui.delete", "gui.focus"), isSelected, event2 -> onDelete())
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
            allFoci = new FilteredList<>(FXCollections.observableArrayList(db.getTblSetFocus().getAllFoci()));
            foci.setItems(allFoci);
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER, lang.format("error.load", "error.subj.focus"), e);
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
                ExceptionAlert.showError(LOGGER, lang.format("error.delete", "error.subj.focus"), e);
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
