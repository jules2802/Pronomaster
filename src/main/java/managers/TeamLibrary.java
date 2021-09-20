package managers;

import dao.TeamDao;
import dao.impl.TeamDaoImpl;
import entities.Team;
import exceptions.TeamNotFoundException;

import java.io.IOException;
import java.util.List;

public class TeamLibrary {

    //Création d'un singleton: une seule Library
    private static class EquipeLibraryHolder {
        private final static TeamLibrary instance = new TeamLibrary();
    }

    private TeamDao teamDao = new TeamDaoImpl();

    public static TeamLibrary getInstance() { return TeamLibrary.EquipeLibraryHolder.instance;
    }

    private TeamLibrary() {
    }

    public List<Team> getAllTeams() throws IOException {return teamDao.getAllTeams();}

    public Team getTeamWithId(int id) throws TeamNotFoundException, IOException {return teamDao.getTeamWithId(id);}
}
