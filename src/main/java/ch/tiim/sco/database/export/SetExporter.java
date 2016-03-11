package ch.tiim.sco.database.export;

import ch.tiim.sco.database.model.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("HardCodedStringLiteral")
public class SetExporter extends XMLExporter<Set> {

    @Override
    public void export(Set data, int id, Document doc, ExportController exp) throws ExportException {
        Element root = getRootElement(doc);
        Element sets = getOrCreateElement(doc, root, "Sets");

        Element setElem = doc.createElement("Set");
        sets.appendChild(setElem);

        int foc = exp.addData(data.getFocus());
        int str = exp.addData(data.getStroke());

        setElem.setAttribute("id", String.valueOf(id));
        appendElement(doc, setElem, "Name", data.getName());
        appendElement(doc, setElem, "Content", data.getContent());
        appendElement(doc, setElem, "Distance1", String.valueOf(data.getDistance1()));
        appendElement(doc, setElem, "Distance2", String.valueOf(data.getDistance2()));
        appendElement(doc, setElem, "Distance3", String.valueOf(data.getDistance3()));
        appendElement(doc, setElem, "FocusID", String.valueOf(foc));
        appendElement(doc, setElem, "StrokeID", String.valueOf(str));
        appendElement(doc, setElem, "Notes", data.getNotes());
        appendElement(doc, setElem, "Interval", String.valueOf(data.getInterval()));
        appendElement(doc, setElem, "IsPause", String.valueOf(data.isPause()));
    }

}
