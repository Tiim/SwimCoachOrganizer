package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCTeamContent extends Table implements ch.tiim.sco.database.TableTeamContent {
    private static final Logger LOGGER = LogManager.getLogger(JDBCTeamContent.class.getName());


    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement get;
    private NamedParameterPreparedStatement getNot;

    public JDBCTeamContent(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        get = db.getPrepStmt(getSql("get"));
        getNot = db.getPrepStmt(getSql("get_not"));
    }

    @Override
    public void addSwimmers(Team t, Swimmer m) throws SQLException {
        add.setInt("team_id", t.getId());
        add.setInt("swimmer_id", m.getId());
        LOGGER.debug(MARKER_QUERRY, add);
        testUpdate(add);
    }

    @Override
    public List<Swimmer> getSwimmers(Team t) throws SQLException {
        get.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, get);
        ResultSet rs = get.executeQuery();
        List<Swimmer> l = new ArrayList<>();
        while (rs.next()) {
            l.add(JDBCSwimmer.getSwimmer(rs));
        }
        return l;
    }

    @Override
    public void deleteSwimmer(Team t, Swimmer m) throws SQLException {
        delete.setInt("team_id", t.getId());
        delete.setInt("swimmer_id", m.getId());
        LOGGER.debug(MARKER_QUERRY, delete);
        testUpdate(delete);
    }

    @Override
    public List<Swimmer> getSwimmersNotInTeam(Team t) throws SQLException {
        getNot.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, getNot);
        ResultSet rs = getNot.executeQuery();
        List<Swimmer> l = new ArrayList<>();
        while (rs.next()) {
            l.add(JDBCSwimmer.getSwimmer(rs));
        }
        return l;
    }
}
