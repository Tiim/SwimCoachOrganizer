package ch.tiim.sco.lenex;

import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.util.NameablePair;
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
        for (Swimmer s : swimmers) {
            List<Result> results = data.getResultsForSwimmer(s);
            for (Result r : results) {
                NameablePair<Swimmer, Result> p = new NameablePair<>(s, r);
                p.setName(String.format("%s %s - %dm %s @%s",
                        s.getFirstName(), s.getLastName(), r.getDistance(), r.getStroke(), r.getSwimTime()));
                pairs.add(p);
            }
        }
        return pairs;
    }
}
