package ch.tiim.sco.database.proc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleException;
import ch.tiim.sco.database.model.ScheduleRule;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcScheduleIt {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcScheduleStream.class);
    private final DatabaseController db;

    ProcScheduleIt(DatabaseController db) {

        this.db = db;
    }

    public List<Pair<LocalDate, ScheduleRule>> getNextTrainings(LocalDate day, int span) throws Exception {
        List<ScheduleRule> schedules = db.getTblSchedule().getAllSchedules();
        List<Pair<LocalDate, ScheduleRule>> rules = new ArrayList<>();
        for (ScheduleRule s : schedules) {
            for (int i = 0; i < span; i++) {

                LocalDate newDay = day.plusDays(i);

                int until = s.getStart().until(newDay).getDays();
                if (until % s.getInterval() == 0) {
                    boolean isException = false;
                    List<ScheduleException> exceptionsForRule = db.getTblSchedule().getExceptionsForRule(s);
                    for (ScheduleException e : exceptionsForRule) {
                        if (e.getDay().equals(newDay)) {
                            isException = true;
                            break;
                        }
                    }
                    if (!isException) {
                        rules.add(new Pair<>(newDay, s));
                    }
                }
            }
        }
        return rules;
    }

}
