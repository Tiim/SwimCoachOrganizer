package ch.tiim.sco.database;

import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;

import java.util.List;

public interface TableTeamContent {
    List<Swimmer> getSwimmers(Team t) throws Exception;

    void addSwimmer(Team t, Swimmer m) throws Exception;

    void deleteSwimmer(Team t, Swimmer m) throws Exception;

    List<Swimmer> getSwimmersNotInTeam(Team t) throws Exception;

    void setSwimmers(Team t, List<Swimmer> swimmers) throws Exception;
}
