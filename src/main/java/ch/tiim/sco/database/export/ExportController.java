package ch.tiim.sco.database.export;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class ExportController {

    private static final Logger LOGGER = LogManager.getLogger(ExportController.class.getName());
    private static final HashMap<Class<? extends Model>, XMLExporter> EXPORTERS = new HashMap<>();

    static {
        EXPORTERS.put(Training.class, new TrainingExporter());
        EXPORTERS.put(Set.class, new SetExporter());
        EXPORTERS.put(SetFocus.class, new FocusExporter());
        EXPORTERS.put(SetStroke.class, new StrokeExporter());
        EXPORTERS.put(Club.class, new ClubExporter());
        EXPORTERS.put(Team.class, new TeamExporter());
        EXPORTERS.put(Swimmer.class, new SwimmerExporter());
        EXPORTERS.put(Result.class, new ResultExporter());
    }

    private final DatabaseController db;
    private final HashMap<Model, Integer> exported = new HashMap<>();
    private final Queue<Pair<Integer, Model>> data = new ArrayDeque<>();
    private int id = 1;

    public ExportController(DatabaseController db) {

        this.db = db;
    }

    public int addData(Model data) {
        if (!exported.containsKey(data)) {
            int id = getNextId();
            exported.put(data, id);
            this.data.add(new Pair<>(id, data));
            return id;
        } else {
            return exported.get(data);
        }
    }

    public int getNextId() {
        return id++;
    }

    public void exportToFile(Path f) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer t = transformerFactory.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(export());
        StreamResult result = new StreamResult(Files.newOutputStream(f));
        t.transform(source, result);
    }

    @SuppressWarnings("unchecked")
    public Document export() throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element root = doc.createElement("SwimCoachOrganizer");
        doc.appendChild(root);
        while (!data.isEmpty()) {
            Pair<Integer, Model> poll = data.poll();
            Model model = poll.getValue();
            XMLExporter ex = EXPORTERS.get(model.getClass());
            if (ex != null) {
                ex.export(model, poll.getKey(), doc, this);
            } else {
                LOGGER.warn("Can't export object of type " + model.getClass() +
                        ": no such exporter registered");
            }
        }
        return doc;
    }

    public DatabaseController getDatabase() {
        return db;
    }
}
