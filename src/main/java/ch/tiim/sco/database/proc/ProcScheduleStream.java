package ch.tiim.sco.database.proc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleRule;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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
    public List<Pair<LocalDate, ScheduleRule>> getNextTrainings(LocalDate day, int span) throws Exception {
        List<ScheduleRule> schedules = db.getTblSchedule().getAllSchedules();

        return schedules.stream()
                //Map every schedule to {1, schedule}, {2, schedule}, ... , {span, schedule}
                .flatMap(scheduleRule -> IntStream.range(0, span + 1).mapToObj(operand -> new Pair<>(operand, scheduleRule)))
                //Filter out every schedule that is not at the day + offset
                .filter(it ->
                        it.getValue().getStart().until(day.plusDays(it.getKey()))
                                .getDays() % it.getValue().getInterval() == 0)
                //Map every pair of (offset, schedule} to {date, schedule}
                .map(it -> new Pair<>(day.plusDays(it.getKey()), it.getValue()))
                //Map every {date, schedule} to {Exceptions,{date,schedule}}
                .map(it -> {
                    try {
                        return new Pair<>(db.getTblSchedule().getExceptionsForRule(it.getValue()), it);
                    } catch (Exception e) {
                        return null;
                    }
                })
                //Leave only instances where no Exception matches
                .filter(it -> !it.getKey().stream()
                        .filter(it2 -> it2.getDay().equals(it.getValue().getKey())).findAny().isPresent())
                //Map {exceptions, {date, schedule}} to {date, schedule}
                .map(Pair::getValue)
                //Collect back to list
                .collect(Collectors.toList());
    }

}
