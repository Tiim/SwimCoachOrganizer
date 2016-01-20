package ch.tiim.sco.database;

import ch.tiim.sco.database.model.SetStroke;

import java.util.List;

public interface TableSetStroke {
    void addSetStroke(SetStroke stroke)throws Exception;

    void updateSetStroke(SetStroke stroke)throws Exception;

    void deleteSetStroke(SetStroke stroke)throws Exception;

    List<SetStroke> getAllStrokes()throws Exception;
}
