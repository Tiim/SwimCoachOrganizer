package ch.tiim.sco.database;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.jdbc.*;
import ch.tiim.sco.util.Debug;
import ch.tiim.sql_xml.SqlLoader;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController implements Closeable {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseController.class.getName());
    private static final String VERSION = "1";

    private final TableSetFocus tblSetFocus;
    private final TableSetStroke tblSetStroke;
    private final TableTraining tblTraining;
    private final TableTrainingContent tblTrainingContent;
    private final TableTeam tblTeam;
    private final TableTeamContent tblTeamContent;
    private final TableSwimmer tblSwimmer;
    private final TableSets tblSet;
    private final TableClub tblClub;
    private final TableClubContent tblClubContent;
    private final TableResult tblResult;

    private final SqlLoader sqlLoader;
    private final Connection conn;
    private boolean initialized = false;

    public DatabaseController(String file) throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Database driver not found", e);
            throw new UnsupportedOperationException("org.h2.Driver not found!", e);
        }

        boolean notExists;
        Path filePath;
        if (!file.equals(":memory:")) {
            filePath = Paths.get(file + ".mv.db");
            notExists = !Files.exists(filePath);
        } else {
            file = "mem:";
            notExists = true;
        }

        conn = DriverManager.getConnection("jdbc:h2:" + file);

        if (notExists) {
            mkDatabase();
        } else {
            initialized = true;
        }

        sqlLoader = new SqlLoader("/ch/tiim/sco/database/queries.sql.xml");
        tblSetFocus = new JDBCSetFocus(this);
        tblSetStroke = new JDBCSetStroke(this);
        tblTraining = new JDBCTraining(this);
        tblSet = new JDBCSets(this);
        tblTrainingContent = new JDBCTrainingContent(this);
        tblTeamContent = new JDBCTeamContent(this);
        tblSwimmer = new JDBCSwimmer(this);
        tblTeam = new JDBCTeam(this);
        tblClub = new JDBCClub(this);
        tblClubContent = new JDBCClubContent(this);
        tblResult = new JDBCResult(this);
    }

    private void mkDatabase() throws SQLException {
        Statement stmt = conn.createStatement();
        String[] cmds = getSql("make.sql").split(";");
        for (String cmd : cmds) {
            stmt.addBatch(cmd);
        }
        stmt.executeBatch();
    }

    private String getSql(String name) {
        try (InputStreamReader is = new InputStreamReader(
                DatabaseController.class.getResourceAsStream(name), Charsets.UTF_8)) {
            return CharStreams.toString(is);
        } catch (IOException e) {
            LOGGER.warn("Could not load file " + name, e);
        }
        return "";
    }

    public NamedParameterPreparedStatement getPrepStmt(String query) throws SQLException {
        return NamedParameterPreparedStatement.createNamedParameterPreparedStatement(conn, query);
    }

    public void initializeDefaultValues() throws SQLException {
        if (!initialized) {
            initialized = true;
        } else {
            LOGGER.info("Default values already initialized.. aborting!");
            return;
        }
        Statement stmt = conn.createStatement();
        String[] cmds = getSql("init.sql").split(";");
        for (String cmd : cmds) {
            stmt.addBatch(cmd);
        }
        stmt.executeBatch();
    }

    @Override
    public void close() throws IOException {
        try {
            conn.close();
        } catch (SQLException e) {
            LOGGER.warn("Can't close connection", e);
        }
    }

    public String debugQuery(String q) throws SQLException {
        LOGGER.info("Running debug query " + q);
        Statement stmt = conn.createStatement();
        return Debug.rs(stmt.executeQuery(q));
    }

    public Connection getConn() {
        return conn;
    }

    public SqlLoader getSqlLoader() {
        return sqlLoader;
    }

    public TableClub getTblClub() {
        return tblClub;
    }

    public TableClubContent getTblClubContent() {
        return tblClubContent;
    }

    public TableResult getTblResult() {
        return tblResult;
    }

    public TableSets getTblSet() {
        return tblSet;
    }

    public TableSetFocus getTblSetFocus() {
        return tblSetFocus;
    }

    public TableSetStroke getTblSetStroke() {
        return tblSetStroke;
    }

    public TableSwimmer getTblSwimmer() {
        return tblSwimmer;
    }

    public TableTeam getTblTeam() {
        return tblTeam;
    }

    public TableTeamContent getTblTeamContent() {
        return tblTeamContent;
    }

    public TableTraining getTblTraining() {
        return tblTraining;
    }

    public TableTrainingContent getTblTrainingContent() {
        return tblTrainingContent;
    }
}
