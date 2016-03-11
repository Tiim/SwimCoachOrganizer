package ch.tiim.sco.database.export;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.util.DurationFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("HardCodedStringLiteral")
public class ResultExporter extends XMLExporter<Result> {
    @Override
    public void export(Result data, int id, Document doc, ExportController exp) throws ExportException {
        Element root = getRootElement(doc);
        Element results = getOrCreateElement(doc, root, "Results");
        Element result = doc.createElement("Result");
        results.appendChild(result);

        int si = 0;
        try {
            si = exp.addData(getSwimmerForResult(data, exp.getDatabase()));
        } catch (Exception e) {
            throw new ExportException(e);
        }


        result.setAttribute("id", String.valueOf(id));
        appendElement(doc, result, "SwimmerID", String.valueOf(si));
        appendElement(doc, result, "Meet", data.getMeet());
        appendElement(doc, result, "MeetDate", data.getMeetDate().toString());
        appendElement(doc, result, "Time", DurationFormatter.format(data.getSwimTime()));
        appendElement(doc, result, "ReactionTime", DurationFormatter.format(data.getSwimTime()));
        appendElement(doc, result, "Stroke", data.getStroke().toString());
        appendElement(doc, result, "Distance", String.valueOf(data.getDistance()));
        appendElement(doc, result, "Course", data.getCourse().toString());
    }

    private Swimmer getSwimmerForResult(Result r, DatabaseController db) throws Exception {
        return db.getTblResult().getSwimmer(r);
    }
}
