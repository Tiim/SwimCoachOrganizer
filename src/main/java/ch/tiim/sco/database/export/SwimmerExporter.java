package ch.tiim.sco.database.export;

import ch.tiim.sco.database.model.Swimmer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SwimmerExporter extends XMLExporter<Swimmer> {

    @Override
    public void export(Swimmer data, int id, Document doc, ExportController exp) throws Exception {
        Element root = getRootElement(doc);
        Element swimmers = getOrCreateElement(doc, root, "Swimmers");
        Element swimmer = doc.createElement("Swimmer");
        swimmers.appendChild(swimmer);

        swimmer.setAttribute("id", String.valueOf(id));
        appendElement(doc, swimmer, "FirstName", data.getFirstName());
        appendElement(doc, swimmer, "LastName", data.getLastName());
        appendElement(doc, swimmer, "Birthday", data.getBirthDay().toString());
        appendElement(doc, swimmer, "Address", data.getAddress());
        appendElement(doc, swimmer, "PhonePrivate", data.getPhonePrivate());
        appendElement(doc, swimmer, "PhoneWork", data.getPhoneWork());
        appendElement(doc, swimmer, "PhoneMobile", data.getPhoneMobile());
        appendElement(doc, swimmer, "Email", data.getEmail());
        appendElement(doc, swimmer, "License", data.getLicense());
        appendElement(doc, swimmer, "IsFemale", String.valueOf(data.isFemale()));
        appendElement(doc, swimmer, "Notes", data.getNotes());
    }
}
