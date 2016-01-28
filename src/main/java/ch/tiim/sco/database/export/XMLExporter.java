package ch.tiim.sco.database.export;

import ch.tiim.sco.database.model.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public abstract class XMLExporter<T extends Model> {

    public abstract void export(T data, int id, Document doc, ExportController exp) throws Exception;

    protected Element getRootElement(Document doc) {
        return doc.getDocumentElement();
    }

    protected void appendElement(Document doc, Element root, String name, String value) {
        Element elem = doc.createElement(name);
        Text text = doc.createTextNode(value == null ? "" : value);
        elem.appendChild(text);
        root.appendChild(elem);
    }

    protected Element getOrCreateElement(Document doc, Element e, String name) {
        NodeList nl = e.getElementsByTagName(name);
        if (nl.getLength() >= 1) {
            return (Element) nl.item(0);
        } else {
            Element el = doc.createElement(name);
            e.appendChild(el);
            return el;
        }
    }

}
