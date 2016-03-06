package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.TableSetStroke;
import ch.tiim.sco.database.model.SetStroke;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCSetStrokeTest {

    private DatabaseController db;
    private TableSetStroke table;
    private SetStroke stroke;

    @Before
    public void setUp() throws Exception {
        stroke = new SetStroke("Freestyle", "Fr", "blub blub");
        db = new DatabaseController(":memory:");
        table = db.getTblSetStroke();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testGetSetStroke() throws Exception {
        table.addSetStroke(stroke);
        SetStroke setStroke = table.getAllStrokes().get(0);
        stroke.setId(setStroke.getId());
        assertEquals(stroke, setStroke);
    }

    @Test
    public void testAddSetStroke() throws Exception {
        table.addSetStroke(stroke);
        table.addSetStroke(stroke);
        table.addSetStroke(stroke);
        table.addSetStroke(stroke);
        table.addSetStroke(stroke);
        table.addSetStroke(stroke);
        assertEquals(6, table.getAllStrokes().size());
    }

    @Test
    public void testUpdateSetStroke() throws Exception {
        table.addSetStroke(stroke);
        SetStroke setStroke = table.getAllStrokes().get(0);
        setStroke.setName("sdflnk asd");
        table.updateSetStroke(setStroke);
        SetStroke setStroke1 = table.getAllStrokes().get(0);
        assertEquals(setStroke, setStroke1);
    }

    @Test
    public void testDeleteSetStroke() throws Exception {
        table.addSetStroke(stroke);
        table.deleteSetStroke(table.getAllStrokes().get(0));
        assertEquals(0, table.getAllStrokes().size());
    }
}