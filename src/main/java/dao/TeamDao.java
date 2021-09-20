package dao;

import entities.Team;
import exceptions.TeamNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TeamDao {

    List<Team> getAllTeams() throws IOException;

    Team getTeamWithId(int id) throws TeamNotFoundException, IOException;
}
