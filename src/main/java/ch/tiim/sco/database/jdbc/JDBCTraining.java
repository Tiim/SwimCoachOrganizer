package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.database.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCTraining extends Table implements ch.tiim.sco.database.TableTraining {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCTraining.class);

    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement getAll;
    private NamedParameterPreparedStatement getFromSchedule;

    public JDBCTraining(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        update = db.getPrepStmt(getSql("update"));
        getAll = db.getPrepStmt(getSql("get_all"));
        getFromSchedule = db.getPrepStmt(getSql("get_from_schedule"));
    }

    @Override
    public void addTraining(Training t) throws SQLException {
        add.setString("date", t.getDate().toString());
        if (t.getTeam() != null) {
            add.setInt("team_id", t.getTeam().getId());
        } else {
            add.setNull("team_id", Types.INTEGER);
        }
        if (t.getSchedule() != null) {
            add.setInt("schedule_id", t.getSchedule().getId());
        } else {
            add.setNull("schedule_id", Types.INTEGER);
        }
        LOGGER.debug(MARKER_QUERY, add.toString());
        testUpdate(add);
        t.setId(getGenKey(add));
    }

    @Override
    public void updateTraining(Training t) throws SQLException {
        update.setString("date", t.getDate().toString());
        update.setInt("id", t.getId());
        if (t.getTeam() != null) {
            update.setInt("team_id", t.getTeam().getId());
        } else {
            update.setNull("team_id", Types.INTEGER);
        }
        if (t.getSchedule() != null) {
            update.setInt("schedule_id", t.getSchedule().getId());
        } else {
            update.setNull("schedule_id", Types.INTEGER);
        }
        LOGGER.debug(MARKER_QUERY, update.toString());
        testUpdate(update);
    }

    @Override
    public void deleteTraining(Training t) throws SQLException {
        delete.setInt("id", t.getId());
        LOGGER.debug(MARKER_QUERY, delete.toString());
        testUpdate(delete);
    }

    @Override
    public Training getTrainingFromSchedule(LocalDate ld, ScheduleRule sr) throws SQLException {
        getFromSchedule.setInt("schedule_id", sr.getId());
        getFromSchedule.setDate("date", Date.valueOf(ld));
        LOGGER.debug(MARKER_QUERY, getFromSchedule.toString());
        ResultSet rs = getFromSchedule.executeQuery();
        if (rs.next()) {
            return getTraining(rs);
        } else {
            return null;
        }
    }

    @Override
    public List<Training> getAllTrainings() throws SQLException {
        ResultSet rs = getAll.executeQuery();
        LOGGER.debug(MARKER_QUERY, getAll.toString());
        List<Training> l = new ArrayList<>();
        while (rs.next()) {
            l.add(getTraining(rs));
        }
        return l;
    }

    static Training getTraining(ResultSet rs) throws SQLException {
        return new Training(
                rs.getInt("training_id"),
                LocalDate.parse(rs.getString("date")),
                rs.getString("name") != null ? JDBCTeam.getTeam(rs) : null,
                rs.getDate("start_date") != null ? JDBCSchedule.getScheduleRule(rs) : null
        );
    }
}
