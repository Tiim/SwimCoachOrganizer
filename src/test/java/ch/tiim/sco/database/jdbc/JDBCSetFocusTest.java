package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.TableSetFocus;
import ch.tiim.sco.database.model.SetFocus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCSetFocusTest {

    private DatabaseController db;
    private TableSetFocus table;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseController(":memory:");
        table = db.getTblSetFocus();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testGetSetFocus() throws Exception {
        table.addSetFocus(focus());
        SetFocus f = table.getAllFoci().get(0);
        SetFocus focus = focus();
        focus.setId(f.getId());
        assertEquals(focus, f);
    }

    @Test
    public void testAddSetFocus() throws Exception {
        table.addSetFocus(focus());
        table.addSetFocus(focus());
        table.addSetFocus(focus());
        table.addSetFocus(focus());
        table.addSetFocus(focus());
        table.addSetFocus(focus());
        assertEquals(6, table.getAllFoci().size());
    }

    @Test
    public void testUpdateSetFocus() throws Exception {
        SetFocus f = focus();
        table.addSetFocus(f);
        f.setName("New Name");
        table.updateSetFocus(f);
        SetFocus fo = table.getAllFoci().get(0);
        assertEquals(f, fo);
    }

    @Test
    public void testDeleteSetFocus() throws Exception {
        table.addSetFocus(focus());
        table.deleteSetFocus(table.getAllFoci().get(0));
        assertEquals(0, table.getAllFoci().size());
    }

    private SetFocus focus() {
        return new SetFocus("Sprint", "Spr", "Swim fast!");
    }
}