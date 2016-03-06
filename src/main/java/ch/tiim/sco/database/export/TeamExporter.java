package ch.tiim.sco.database.export;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("HardCodedStringLiteral")
public class TeamExporter extends XMLExporter<Team> {

    @Override
    public void export(Team data, int id, Document doc, ExportController exp) throws Exception {
        Element root = getRootElement(doc);
        Element teams = getOrCreateElement(doc, root, "Teams");
        Element team = doc.createElement("Team");
        teams.appendChild(team);

        team.setAttribute("id", String.valueOf(id));
        appendElement(doc, team, "Name", data.getName());

        Element swimmers = doc.createElement("SwimmersID");
        team.appendChild(swimmers);
        List<Integer> ids = getSwimmers(data, exp.getDatabase()).stream()
                .map(exp::addData)
                .collect(Collectors.toList());
        for (Integer i : ids) {
            Element swimmer = doc.createElement("SwimmerID");
            swimmers.appendChild(swimmer);
            Text text = doc.createTextNode(String.valueOf(i));
            swimmer.appendChild(text);
        }
    }

    private List<Swimmer> getSwimmers(Team data, DatabaseController db) throws Exception {
        return db.getTblTeamContent().getSwimmers(data);
    }
}
