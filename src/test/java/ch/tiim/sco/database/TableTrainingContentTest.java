package ch.tiim.sco.database;

import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.Training;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableTrainingContentTest {
    private static final Logger LOGGER = LogManager.getLogger(TableTrainingContentTest.class.getName());
    private DatabaseController db;
    private Training t;
    private IndexedSet s1;
    private IndexedSet s2;
    private IndexedSet s3;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseController(":memory:");
        t = new Training("Training");
        s1 = iset(1);
        s2 = iset(2);
        s3 = iset(3);
        db.getTblTraining().addTraining(t);
        db.getTblSet().addSet(s1.getSet());
        db.getTblSet().addSet(s2.getSet());
        db.getTblSet().addSet(s3.getSet());
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testGetSetsForTraining() throws Exception {
        db.getTblTrainingContent().addSetToTraining(t, s1.getSet(), s1.getIndex());
        db.getTblTrainingContent().addSetToTraining(t, s2.getSet(), s2.getIndex());
        db.getTblTrainingContent().addSetToTraining(t, s3.getSet(), s3.getIndex());
        List<IndexedSet> sets = db.getTblTrainingContent().getSetsForTraining(t);
        assertEquals(3, sets.size());
        assertTrue(sets.contains(s1));
        assertTrue(sets.contains(s2));
        assertTrue(sets.contains(s3));
    }

    @Test
    public void testAddSetToTraining() throws Exception {
        // Tested in testGetSetsForTraining
    }

    @Test
    public void testDeleteSet() throws Exception {
        db.getTblTrainingContent().addSetToTraining(t, s1.getSet(), s1.getIndex());
        db.getTblTrainingContent().addSetToTraining(t, s2.getSet(), s2.getIndex());
        db.getTblTrainingContent().deleteSet(t, s1.getSet(), s1.getIndex());
        List<IndexedSet> sets = db.getTblTrainingContent().getSetsForTraining(t);
        assertEquals(1, sets.size());
        assertTrue(sets.contains(s2));
    }

    @Test
    public void testUpdateIndexUp() throws Exception {
        db.getTblTrainingContent().addSetToTraining(t, s1.getSet(), s1.getIndex());
        db.getTblTrainingContent().addSetToTraining(t, s2.getSet(), s2.getIndex());
        List<IndexedSet> before = db.getTblTrainingContent().getSetsForTraining(t);
        db.getTblTrainingContent().updateIndex(t, s2.getIndex(), true);
        List<IndexedSet> sets = db.getTblTrainingContent().getSetsForTraining(t);
        Assert.assertNotEquals(before, sets);
    }

    private IndexedSet iset(int index) {
        return new IndexedSet(index,
                new Set(
                        "Set",
                        "Content",
                        1, 1, 400,
                        50,
                        null, null,
                        "This is very slow",
                        20, true
                ));
    }
}