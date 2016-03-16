package ch.tiim.sco.database.export;

import ch.tiim.sco.database.model.ScheduleRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScheduleExporter extends XMLExporter<ScheduleRule> {

    @Override
    public void export(ScheduleRule data, int id, Document doc, ExportController exp) throws ExportException {
        Element root = getRootElement(doc);
        Element rules = getOrCreateElement(doc, root, "Schedules");

        Element elem = doc.createElement("Schedule");
        rules.appendChild(elem);

        int team = exp.addData(data.getTeam());

        elem.setAttribute("id", String.valueOf(id));
        appendElement(doc, elem, "TeamID", String.valueOf(team));
        appendElement(doc, elem, "Start", String.valueOf(data.getStart()));
        appendElement(doc, elem, "Duration", String.valueOf(data.getDuration()));
        appendElement(doc, elem, "Time", String.valueOf(data.getTime()));
        appendElement(doc, elem, "Interval", String.valueOf(data.getInterval()));
    }
}
