package ch.tiim.sco.database.export;

import ch.tiim.sco.database.model.SetStroke;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("HardCodedStringLiteral")
public class StrokeExporter extends XMLExporter<SetStroke> {

    @Override
    public void export(SetStroke data, int id, Document doc, ExportController exp) throws Exception {
        Element root = getRootElement(doc);
        Element strokes = getOrCreateElement(doc, root, "Strokes");

        Element stroke = doc.createElement("Stroke");
        strokes.appendChild(stroke);
        stroke.setAttribute("id", String.valueOf(id));
        appendElement(doc, stroke, "Name", data.getName());
        appendElement(doc, stroke, "Abbr", data.getAbbr());
        appendElement(doc, stroke, "Notes", data.getNotes());
    }
}
