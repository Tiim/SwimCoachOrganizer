package ch.tiim.sco.database;

import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;

import java.util.List;

public interface TableClubContent {
    void addTeam(Club c, Team t) throws Exception;

    void deleteTeam(Club c, Team t) throws Exception;

    List<Team> getTeams(Club c) throws Exception;

    List<Team> getNotTeams(Club c) throws Exception;

    void setTeams(Club club, List<Team> teams) throws Exception;
}
