package ch.tiim.sco.gui.birthday;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.utils.ModelCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class BirthdayPresenter extends Page {
    private static final Logger LOGGER = LogManager.getLogger(BirthdayPresenter.class.getName());
    @FXML
    private ListView<Swimmer> listThisWeek;

    @FXML
    private ListView<Swimmer> listNextWeek;

    @FXML
    private ListView<Swimmer> listThirtyDays;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<Swimmer> thisWeek = FXCollections.observableArrayList();
    private ObservableList<Swimmer> nextWeek = FXCollections.observableArrayList();
    private ObservableList<Swimmer> thirtyDays = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        listThisWeek.setItems(thisWeek);
        listNextWeek.setItems(nextWeek);
        listThirtyDays.setItems(thirtyDays);
    }

    @Inject
    private void injected() {
        update();
    }

    private void update() {
        LocalDate today = LocalDate.now();
        LocalDate endThisWeek = LocalDate.ofYearDay(today.getYear(),
                today.getDayOfYear() + 7 - today.getDayOfWeek().getValue());
        LocalDate startNextWeek = endThisWeek.plusDays(1);
        LocalDate endNextWeek = endThisWeek.plusDays(7);
        LocalDate startOfThirtyDays = endNextWeek.plusDays(1);
        LocalDate endOfThirtyDays = LocalDate.ofYearDay(today.getYear(), today.getDayOfYear() + 30);

        Callback<ListView<Swimmer>, ListCell<Swimmer>> c = param -> new ModelCell<>();

        listNextWeek.setCellFactory(c);
        listThisWeek.setCellFactory(c);
        listThirtyDays.setCellFactory(c);

        try {
            thisWeek.setAll(db.getTblSwimmer().getSwimmersWithBirthdayBetween(today, endThisWeek));
            nextWeek.setAll(db.getTblSwimmer().getSwimmersWithBirthdayBetween(startNextWeek, endNextWeek));
            thirtyDays.setAll(db.getTblSwimmer().getSwimmersWithBirthdayBetween(startOfThirtyDays, endOfThirtyDays));
        } catch (Exception e) {
            LOGGER.warn("Failed to read from database", e);
        }
    }

    @Override
    public void opened() {
        update();
    }

    @Override
    public String getName() {
        return "Next Birthdays";
    }
}
