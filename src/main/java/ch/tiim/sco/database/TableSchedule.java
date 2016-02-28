package ch.tiim.sco.database;

import ch.tiim.sco.database.model.ScheduleException;
import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.database.model.Team;

import java.util.List;

public interface TableSchedule {

    void addSchedule(ScheduleRule schedule) throws Exception;

    void deleteSchedule(ScheduleRule schedule) throws Exception;

    void updateSchedule(ScheduleRule schedule) throws Exception;

    List<ScheduleRule> getSchedules(Team team) throws Exception;

    void addException(ScheduleException exception) throws Exception;

    void deleteException(ScheduleException exception) throws Exception;

    void updateException(ScheduleException exception) throws Exception;

    List<ScheduleException> getExceptionsForRule(ScheduleRule rule) throws Exception;

    List<ScheduleRule> getAllSchedules() throws Exception;

}
