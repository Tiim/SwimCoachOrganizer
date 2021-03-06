package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.TableResult;
import ch.tiim.sco.database.model.Course;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Stroke;
import ch.tiim.sco.database.model.Swimmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCResult extends Table implements TableResult {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCResult.class);

    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement get;
    private NamedParameterPreparedStatement getSwimmer;

    public JDBCResult(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        get = db.getPrepStmt(getSql("get"));
        update = db.getPrepStmt(getSql("update"));
        getSwimmer = db.getPrepStmt(getSql("get_swimmer"));
    }

    //TODO: add test for null parameters
    @Override
    public void addResult(Swimmer s, Result r) throws SQLException {
        add.setInt("swimmer_id", s.getId());
        add.setString("meet", r.getMeet());
        add.setString("meet_date", r.getMeetDate().toString());
        add.setLong("swim_time", r.getSwimTime().toMillis());
        if (r.getReactionTime() != null) {
            add.setInt("reaction_time", (int) r.getReactionTime().toMillis());
        } else {
            add.setNull("reaction_time", Types.INTEGER);
        }
        add.setString("stroke", r.getStroke().toString());
        add.setInt("distance", r.getDistance());
        add.setString("course", r.getCourse().toString());
        testUpdate(add);
        r.setId(getGenKey(add));
    }

    @Override
    public void updateResult(Result r) throws SQLException {
        update.setString("meet", r.getMeet());
        update.setString("meet_date", r.getMeetDate().toString());
        update.setLong("swim_time", r.getSwimTime().toMillis());
        update.setInt("reaction_time", (int) r.getReactionTime().toMillis());
        update.setString("stroke", r.getStroke().toString());
        update.setInt("distance", r.getDistance());
        update.setString("course", r.getCourse().toString());
        update.setInt("id", r.getId());
        testUpdate(update);
    }

    @Override
    public void deleteResult(Result r) throws SQLException {
        delete.setInt("id", r.getId());
        testUpdate(delete);
    }

    @Override
    public List<Result> getResults(Swimmer s) throws SQLException {
        get.setInt("id", s.getId());
        LOGGER.debug(MARKER_QUERY, get.toString());
        ResultSet rs = get.executeQuery();
        List<Result> l = new ArrayList<>();
        while (rs.next()) {
            l.add(getResult(rs));
        }
        return l;
    }

    @Override
    public Swimmer getSwimmer(Result r) throws Exception {
        getSwimmer.setInt("result_id", r.getId());
        ResultSet rs = getSwimmer.executeQuery();
        rs.next();
        return JDBCSwimmer.getSwimmer(rs);
    }

    static Result getResult(ResultSet rs) throws SQLException {
        return new Result(
                rs.getInt("result_id"),
                rs.getString("meet"),
                LocalDate.parse(rs.getString("meet_date")),
                Duration.ofMillis(rs.getLong("swim_time")),
                Duration.ofMillis(rs.getInt("reaction_time")),
                Stroke.valueOf(rs.getString("stroke")),
                rs.getInt("distance"),
                Course.valueOf(rs.getString("course"))
        );
    }
}
