package ch.tiim.sco.database.proc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleRule;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcScheduleStream implements ch.tiim.sco.database.ProcSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcScheduleStream.class);
    private final DatabaseController db;

    public ProcScheduleStream(DatabaseController db) {

        this.db = db;
    }

    @Override
    public List<ScheduleRule> getTrainingsForDay(LocalDate day) throws Exception {
        List<ScheduleRule> schedules = db.getTblSchedule().getAllSchedules();

        return schedules.stream()
                //Filter out every schedule that is not at the day
                .filter(it -> ChronoUnit.DAYS.between(it.getStart(), day) % it.getInterval() == 0)
                //Map every {schedule} to {Exceptions[],schedule}
                .map(it -> {
                    try {
                        return new Pair<>(db.getTblSchedule().getExceptionsForRule(it), it);
                    } catch (Exception e) {
                        return null;
                    }
                })
                //Leave only instances where no Exception matches
                .filter(it -> !it.getKey().stream()
                        .filter(it2 -> it2.getDay().equals(day)).findAny().isPresent())
                //Map {exceptions[], schedule} to {schedule}
                .map(Pair::getValue)
                //Collect back to list
                .collect(Collectors.toList());
    }

}
