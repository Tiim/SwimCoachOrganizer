package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.TableSets;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.SetFocus;
import ch.tiim.sco.database.model.SetStroke;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
public class JDBCSetsTest {
    private TableSets sets;

    private DatabaseController db;

    @Before
    public void setup() throws SQLException {
        db = new DatabaseController(":memory:");
        sets = db.getTblSet();
    }

    @After
    public void tearDown() throws IOException {
        db.close();
    }

    @Test
    public void testInsert() throws Exception {
        Set s = set();
        sets.addSet(s);
        Set result = sets.getAllSets().get(0);
        assertEquals(s, result);
    }

    private Set set() {
        return new Set(
                "TestName",
                "Content",
                1, 2, 3, 99,
                null, null,
                "Test Note",
                10, true
        );
    }

    @Test
    public void testUpdate() throws Exception {
        sets.addSet(set());
        Set s = sets.getAllSets().get(0);
        s.setContent("123Test");
        sets.updateSet(s);
        Set s2 = sets.getAllSets().get(0);
        assertEquals(s, s2);
    }

    @Test
    public void testUpdateUnicode() throws Exception {
        sets.addSet(set());
        Set s = sets.getAllSets().get(0);
        s.setContent("\uD83D\uDE35 -- This is an emoji xoxo");
        sets.updateSet(s);
        Set s2 = sets.getAllSets().get(0);
        assertEquals(s, s2);
    }

    //Tests bug fixed in 6765ece006e5e70fc0ad479f78a2066f4f7db932
    @Test
    public void testGetAllSets() throws Exception {
        SetFocus sf = new SetFocus("Test focus", "tf", "Focus notes");
        SetStroke sfo = new SetStroke("Test stroke", "tfo", "Stroke Notes");
        db.getTblSetFocus().addSetFocus(sf);
        db.getTblSetStroke().addSetStroke(sfo);
        Set s1 = set();
        s1.setFocus(sf);
        s1.setStroke(sfo);
        sets.addSet(s1);

        Assert.assertEquals(s1, sets.getAllSets().get(0));
    }
}
