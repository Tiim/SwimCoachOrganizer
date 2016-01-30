package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCClub extends Table implements ch.tiim.sco.database.TableClub {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCClub.class);

    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement getAll;


    public JDBCClub(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        update = db.getPrepStmt(getSql("update"));
        getAll = db.getPrepStmt(getSql("get_all"));
    }

    @Override
    public void addClub(Club c) throws SQLException {
        add.setString("name", c.getName());
        add.setString("nameShort", c.getNameShort());
        add.setString("nameEn", c.getNameEn());
        add.setString("nameShortEn", c.getNameShortEn());
        add.setString("code", c.getCode());
        add.setString("nationality", c.getNationality());
        add.setInt("externId", c.getExternId());
        LOGGER.debug(MARKER_QUERRY, add.toString());
        testUpdate(add);
        c.setId(getGenKey(add));
    }

    @Override
    public void deleteClub(Club c) throws SQLException {
        delete.setInt("id", c.getId());
        LOGGER.debug(MARKER_QUERRY, delete.toString());
        testUpdate(delete);
    }

    @Override
    public void updateClub(Club c) throws SQLException {
        update.setString("name", c.getName());
        update.setString("nameShort", c.getNameShort());
        update.setString("nameEn", c.getNameEn());
        update.setString("nameShortEn", c.getNameShortEn());
        update.setString("code", c.getCode());
        update.setString("nationality", c.getNationality());
        update.setInt("externId", c.getExternId());
        update.setInt("id", c.getId());
        LOGGER.debug(MARKER_QUERRY, update.toString());
        testUpdate(update);
    }

    @Override
    public List<Club> getAll() throws SQLException {
        LOGGER.debug(MARKER_QUERRY, getAll.toString());
        ResultSet rs = getAll.executeQuery();
        List<Club> l = new ArrayList<>();
        while (rs.next()) {
            l.add(getClub(rs));
        }
        return l;
    }

    static Club getClub(ResultSet rs) throws SQLException {
        return new Club(
                rs.getInt("club_id"),
                rs.getString("name"),
                rs.getString("name_short"),
                rs.getString("name_en"),
                rs.getString("name_short_en"),
                rs.getString("code"),
                rs.getString("nationality"),
                rs.getInt("extern_id")
        );
    }
}
