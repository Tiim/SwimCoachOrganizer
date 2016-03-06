package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.TableSchedule;
import ch.tiim.sco.database.model.ScheduleException;
import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.database.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCSchedule extends Table implements TableSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCSchedule.class);
    private NamedParameterPreparedStatement addSchedule;
    private NamedParameterPreparedStatement deleteSchedule;
    private NamedParameterPreparedStatement updateSchedule;
    private NamedParameterPreparedStatement getSchedule;
    private NamedParameterPreparedStatement getAllSchedules;
    private NamedParameterPreparedStatement addException;
    private NamedParameterPreparedStatement deleteException;
    private NamedParameterPreparedStatement updateException;
    private NamedParameterPreparedStatement getExceptionForRule;


    public JDBCSchedule(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        addSchedule = db.getPrepStmt(getSql("add_schedule"));
        deleteSchedule = db.getPrepStmt(getSql("delete_schedule"));
        updateSchedule = db.getPrepStmt(getSql("update_schedule"));
        getSchedule = db.getPrepStmt(getSql("get_schedule"));
        getAllSchedules = db.getPrepStmt(getSql("get_all_schedules"));
        addException = db.getPrepStmt(getSql("add_exception"));
        deleteException = db.getPrepStmt(getSql("delete_exception"));
        updateException = db.getPrepStmt(getSql("update_exception"));
        getExceptionForRule = db.getPrepStmt(getSql("get_exception_for_rule"));
    }

    @Override
    public void addSchedule(ScheduleRule s) throws Exception {
        addSchedule.setDate("start_date", Date.valueOf(s.getStart()));
        addSchedule.setTime("start_time", Time.valueOf(s.getTime()));
        addSchedule.setInt("inter", s.getInterval());
        addSchedule.setInt("duration", s.getDuration());
        addSchedule.setInt("team_id", s.getTeam().getId());
        LOGGER.debug(MARKER_QUERY, addSchedule.toString());
        testUpdate(addSchedule);
        s.setId(getGenKey(addSchedule));
    }

    @Override
    public void deleteSchedule(ScheduleRule s) throws Exception {
        deleteSchedule.setInt("schedule_id", s.getId());
        LOGGER.debug(MARKER_QUERY, deleteSchedule.toString());
        testUpdate(deleteSchedule);
    }

    @Override
    public void updateSchedule(ScheduleRule s) throws Exception {
        updateSchedule.setInt("schedule_id", s.getId());
        updateSchedule.setDate("start_date", Date.valueOf(s.getStart()));
        updateSchedule.setTime("start_time", Time.valueOf(s.getTime()));
        updateSchedule.setInt("inter", s.getInterval());
        updateSchedule.setInt("duration", s.getDuration());
        updateSchedule.setInt("team_id", s.getTeam().getId());
        LOGGER.debug(MARKER_QUERY, updateSchedule.toString());
        testUpdate(updateSchedule);
    }

    @Override
    public List<ScheduleRule> getSchedules(Team team) throws Exception {
        getSchedule.setInt("team_id", team.getId());
        LOGGER.debug(MARKER_QUERY, getSchedule.toString());
        ResultSet rs = getSchedule.executeQuery();
        List<ScheduleRule> s = new ArrayList<>();
        while (rs.next()) {
            s.add(getScheduleRule(rs));
        }
        return s;
    }

    @Override
    public List<ScheduleRule> getAllSchedules() throws Exception {
        LOGGER.debug(MARKER_QUERY, getAllSchedules.toString());
        ResultSet rs = getAllSchedules.executeQuery();
        List<ScheduleRule> s = new ArrayList<>();
        while (rs.next()) {
            s.add(getScheduleRule(rs));
        }
        return s;
    }

    @Override
    public void addException(ScheduleException e) throws Exception {
        addException.setInt("schedule_id", e.getSchedule().getId());
        addException.setDate("day", Date.valueOf(e.getDay()));
        LOGGER.debug(MARKER_QUERY, addException.toString());
        testUpdate(addException);
        e.setId(getGenKey(addException));
    }

    @Override
    public void deleteException(ScheduleException e) throws Exception {
        deleteException.setInt("exception_id", e.getId());
        LOGGER.debug(MARKER_QUERY, deleteException.toString());
        testUpdate(deleteException);
    }

    @Override
    public void updateException(ScheduleException e) throws Exception {
        updateException.setInt("exception_id", e.getId());
        updateException.setInt("schedule_id", e.getSchedule().getId());
        updateException.setDate("day", Date.valueOf(e.getDay()));
        LOGGER.debug(MARKER_QUERY, updateException.toString());
        testUpdate(updateException);
    }

    @Override
    public List<ScheduleException> getExceptionsForRule(ScheduleRule rule) throws Exception {
        getExceptionForRule.setInt("schedule_id", rule.getId());
        LOGGER.debug(MARKER_QUERY, updateException.toString());
        ResultSet rs = getExceptionForRule.executeQuery();
        List<ScheduleException> exceptions = new ArrayList<>();
        while (rs.next()) {
            exceptions.add(getScheduleException(rs));
        }
        return exceptions;
    }

    static ScheduleException getScheduleException(ResultSet rs) throws Exception {
        return new ScheduleException(
                rs.getInt("exception_id"),
                getScheduleRule(rs),
                rs.getDate("day").toLocalDate()
        );
    }

    static ScheduleRule getScheduleRule(ResultSet rs) throws SQLException {
        Team t = JDBCTeam.getTeam(rs);
        return new ScheduleRule(
                rs.getInt("schedule_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getInt("inter"),
                rs.getInt("duration"),
                t
        );
    }
}
