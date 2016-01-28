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
public class TrainingExporter extends XMLExporter<Training> {

    private DatabaseController db;

    public TrainingExporter() {
        super();
    }

    @Override
    public void export(Training data, int id, Document doc, ExportController exp) throws Exception {
        db = exp.getDatabase();
        Element root = getRootElement(doc);
        Element trainings = getOrCreateElement(doc, root, "Trainings");

        Element training = doc.createElement("Training");
        trainings.appendChild(training);

        training.setAttribute("id", String.valueOf(id));
        appendElement(doc, training, "Name", data.getName());

        List<Pair<Integer, IndexedSet>> sets = getSetsForTraining(data)
                .stream()
                .map(it -> new Pair<>(exp.addData(it.getSet()), it))
                .collect(Collectors.toList());
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
