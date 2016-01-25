package ch.tiim.sco.database;

import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.Training;

import java.util.List;

public interface TableTrainingContent {
    List<IndexedSet> getSets(Training training) throws Exception;

    void addSet(Training t, Set set, int index) throws Exception;

    void deleteSet(Training t, Set s, int index) throws Exception;

    void updateIndex(Training tr, int index, boolean up) throws Exception;

    void setSets(Training tr, List<IndexedSet> items) throws Exception;
}
