package ch.tiim.sco.database;

import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;

import java.util.List;

public interface TableResult {
    void addResult(Swimmer s, ch.tiim.sco.database.model.Result r) throws Exception;

    void updateResult(ch.tiim.sco.database.model.Result r) throws Exception;

    void deleteResult(ch.tiim.sco.database.model.Result r) throws Exception;

    List<ch.tiim.sco.database.model.Result> getResults(Swimmer m) throws Exception;

    Swimmer getSwimmer(Result r) throws Exception;
}
