package ch.tiim.sco.database.export;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("HardCodedStringLiteral")
public class ClubExporter extends XMLExporter<Club> {

    @Override
    public void export(Club data, int id, Document doc, ExportController exp) throws Exception {
        Element root = getRootElement(doc);
        Element clubs = getOrCreateElement(doc, root, "Clubs");
        Element club = doc.createElement("Club");
        clubs.appendChild(club);

        club.setAttribute("id", String.valueOf(id));

        appendElement(doc, club, "Name", data.getName());
        appendElement(doc, club, "NameShort", data.getNameShort());
        appendElement(doc, club, "NameEn", data.getNameEn());
        appendElement(doc, club, "NameShortEn", data.getNameShortEn());
        appendElement(doc, club, "Nationality", data.getNationality());
        appendElement(doc, club, "Code", data.getCode());
        appendElement(doc, club, "ClubID", String.valueOf(data.getId()));
        appendElement(doc, club, "ExternId", String.valueOf(data.getExternId()));

        Element teams = doc.createElement("TeamsID");
        club.appendChild(teams);
        List<Integer> ids = getTeams(data, exp.getDatabase()).stream()
                .map(exp::addData)
                .collect(Collectors.toList());
        for (Integer i : ids) {
            Element team = doc.createElement("TeamID");
            teams.appendChild(team);
            Text text = doc.createTextNode(String.valueOf(i));
            team.appendChild(text);
        }
    }

    private List<Team> getTeams(Club data, DatabaseController db) throws Exception {
        return db.getTblClubContent().getTeams(data);
    }

}
