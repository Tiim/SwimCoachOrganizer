package ch.tiim.sco.database.export;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.Training;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exports all trainings and it's dependent sets, setfoci and setstrokes
 * to xml
 */
@SuppressWarnings("HardCodedStringLiteral")
public class TrainingExporter extends XMLExporter<Training> {

    private DatabaseController db;

    public TrainingExporter() {
        super();
    }

    @Override
    public void export(Training data, int id, Document doc, ExportController exp) throws ExportException {
        db = exp.getDatabase();
        Element root = getRootElement(doc);
        Element trainings = getOrCreateElement(doc, root, "Trainings");

        Element training = doc.createElement("Training");
        trainings.appendChild(training);

        int team = 0;
        if (data.getTeam() != null) {
            team = exp.addData(data.getTeam());
        }
        int schedule = 0;
        if (data.getSchedule() != null) {
            schedule = exp.addData(data.getSchedule());
        }

        training.setAttribute("id", String.valueOf(id));
        appendElement(doc, training, "Date", data.getDate().toString());
        appendElement(doc, training, "TeamID", String.valueOf(team));
        appendElement(doc, training, "ScheduleID", String.valueOf(schedule));

        List<Pair<Integer, IndexedSet>> sets = null;
        try {
            sets = getSetsForTraining(data)
                    .stream()
                    .map(it -> new Pair<>(exp.addData(it.getSet()), it))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ExportException(e);
        }
        Element setsElement = doc.createElement("SetsID");
        training.appendChild(setsElement);
        int totalDistance = sets.stream().mapToInt(it -> it.getValue().getSet().getTotalDistance()).sum();
        appendElement(doc, training, "Distance", String.valueOf(totalDistance));
        insertSetIds(doc, setsElement, sets);
    }

    protected List<IndexedSet> getSetsForTraining(Training t) throws Exception {
        return db.getTblTrainingContent().getSets(t);
    }

    private void insertSetIds(Document doc, Element setsElement, List<Pair<Integer, IndexedSet>> sets) {
        for (Pair<Integer, IndexedSet> set : sets) {
            Element setElement = doc.createElement("SetID");
            setsElement.appendChild(setElement);
            Text txt = doc.createTextNode(String.valueOf(set.getKey()));
            setElement.appendChild(txt);
            setElement.setAttribute("index", String.valueOf(set.getValue().getIndex()));
        }
    }
}
