package ch.tiim.sco.lenex;

import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.lenex.model.Lenex;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ImportResultsTask extends Task<List<Pair<Swimmer, Result>>> {

    private final List<Swimmer> swimmers;
    private final Lenex lenex;

    public ImportResultsTask(List<Swimmer> swimmers, Lenex lenex) {
        this.swimmers = swimmers;
        this.lenex = lenex;
    }

    @Override
    protected List<Pair<Swimmer, Result>> call() throws Exception {
        ResultsData data = new ResultsData(lenex);
        List<Pair<Swimmer, Result>> pairs = new ArrayList<>();
        for (int i = 0, swimmersSize = swimmers.size(); i < swimmersSize; i++) {
            updateProgress(i, swimmersSize);
            Swimmer s = swimmers.get(i);
            List<Result> results = data.getResultsForSwimmer(s);
            for (Result r : results) {
                Pair<Swimmer, Result> p = new Pair<>(s, r);
                pairs.add(p);
            }
        }
        return pairs;
    }
}
