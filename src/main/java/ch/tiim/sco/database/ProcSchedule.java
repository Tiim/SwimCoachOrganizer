package ch.tiim.sco.database;

import ch.tiim.sco.database.model.ScheduleRule;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.List;

public interface ProcSchedule {
    List<Pair<LocalDate, ScheduleRule>> getNextTrainings(LocalDate day, int span) throws Exception;
}
