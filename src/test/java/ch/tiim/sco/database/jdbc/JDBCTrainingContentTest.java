package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.database.model.Training;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JDBCTrainingContentTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCTrainingContentTest.class);
    private DatabaseController db;
    private Training t;
    private IndexedSet s1;
    private IndexedSet s2;
    private IndexedSet s3;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseController(":memory:");
        Team team = new Team("Test Team");
        t = new Training(LocalDate.now(), team);
        s1 = iset(1);
        s2 = iset(2);
        s3 = iset(3);
        db.getTblTraining().addTraining(t);
        db.getTblSet().addSet(s1.getSet());
        db.getTblSet().addSet(s2.getSet());
        db.getTblSet().addSet(s3.getSet());
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

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testGetSetsForTraining() throws Exception {
        db.getTblTrainingContent().addSet(t, s1.getSet(), s1.getIndex());
        db.getTblTrainingContent().addSet(t, s2.getSet(), s2.getIndex());
        db.getTblTrainingContent().addSet(t, s3.getSet(), s3.getIndex());
        List<IndexedSet> sets = db.getTblTrainingContent().getSets(t);
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
        db.getTblTrainingContent().addSet(t, s1.getSet(), s1.getIndex());
        db.getTblTrainingContent().addSet(t, s2.getSet(), s2.getIndex());
        db.getTblTrainingContent().deleteSet(t, s1.getIndex());
        List<IndexedSet> sets = db.getTblTrainingContent().getSets(t);
        assertEquals(1, sets.size());
        assertTrue(sets.contains(s2));
    }

    @Test
    public void testUpdateIndexUp() throws Exception {

        db.getTblTrainingContent().addSet(t, s1.getSet(), s1.getIndex());
        db.getTblTrainingContent().addSet(t, s2.getSet(), s2.getIndex());

        List<IndexedSet> before = db.getTblTrainingContent().getSets(t);
        db.getTblTrainingContent().updateIndex(t, s2.getIndex(), true);
        List<IndexedSet> sets = db.getTblTrainingContent().getSets(t);
        Assert.assertNotEquals(before, sets);
        IndexedSet i1 = sets.get(0).getSet().getId() == 1 ? sets.get(0) : sets.get(1);
        IndexedSet i2 = sets.get(1).getSet().getId() == 2 ? sets.get(1) : sets.get(0);
        try {
            Assert.assertEquals(2, i1.getIndex());
            Assert.assertEquals(1, i2.getIndex());
        } catch (AssertionError e) {
            LOGGER.warn(s1.toString());
            LOGGER.warn(s2.toString());
            LOGGER.warn(i1.toString());
            LOGGER.warn(i2.toString());
            throw e;
        }
    }
}