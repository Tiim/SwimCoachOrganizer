package ch.tiim.trainingmanager.gui.birthday;

import ch.tiim.inject.Inject;
import ch.tiim.log.Log;
import ch.tiim.trainingmanager.database.DatabaseController;
import ch.tiim.trainingmanager.database.model.TeamMember;
import ch.tiim.trainingmanager.gui.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.time.LocalDate;

public class BirthdayPresenter extends Page{
    private static final Log LOGGER = new Log(BirthdayPresenter.class);
    @FXML
    private ListView<TeamMember> listThisWeek;

    @FXML
    private ListView<TeamMember> listNextWeek;

    @FXML
    private ListView<TeamMember> listThirtyDays;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private ObservableList<TeamMember> thisWeek = FXCollections.observableArrayList();
    private ObservableList<TeamMember> nextWeek = FXCollections.observableArrayList();
    private ObservableList<TeamMember> thirtyDays = FXCollections.observableArrayList();

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

        try {
            thisWeek.setAll(db.getTblTeamMember().getMembersWithBirthdayBetween(today,endThisWeek));
            nextWeek.setAll(db.getTblTeamMember().getMembersWithBirthdayBetween(startNextWeek,endNextWeek));
            thirtyDays.setAll(db.getTblTeamMember().getMembersWithBirthdayBetween(startOfThirtyDays,endOfThirtyDays));
        } catch (SQLException e) {
            LOGGER.warning(e);
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
