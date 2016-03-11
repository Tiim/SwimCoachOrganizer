package ch.tiim.sco.database.export;

import ch.tiim.sco.database.model.SetFocus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("HardCodedStringLiteral")
public class FocusExporter extends XMLExporter<SetFocus> {

    @Override
    public void export(SetFocus data, int id, Document doc, ExportController exp) throws ExportException {
        Element root = getRootElement(doc);
        Element foci = getOrCreateElement(doc, root, "Foci");

        Element focus = doc.createElement("Focus");
        foci.appendChild(focus);
        focus.setAttribute("id", String.valueOf(id));
        appendElement(doc, focus, "Name", data.getName());
        appendElement(doc, focus, "Abbr", data.getAbbr());
        appendElement(doc, focus, "Notes", data.getNotes());
    }
}
