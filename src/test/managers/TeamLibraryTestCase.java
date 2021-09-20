package managers;

import dao.TeamDao;
import entities.Team;
import exceptions.TeamNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TeamLibraryTestCase {

    @InjectMocks
    private TeamLibrary teamLibrary= TeamLibrary.getInstance();

    @Mock
    private TeamDao teamDao;

    @Test
    public void shouldGetAllTeams() throws IOException {
        //GIVEN
        Team team1=new Team(1,"PSG");
        Team team2=new Team(2,"OM");
        Team team3=new Team(3,"LYON");

        List<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);

        Mockito.when(teamDao.getAllTeams()).thenReturn(teams);

        //WHEN
        List<Team> allTeams = teamLibrary.getAllTeams();

        //THEN
        assertThat(allTeams).isEqualTo(teams);

    }

    @Test
    public void shouldGetTeamWithId() throws TeamNotFoundException, IOException {
        //GIVEN
        int id=1;
        Team team1=new Team(1,"PSG");

        Mockito.when(teamDao.getTeamWithId(id)).thenReturn(team1);

        //WHEN
        Team teamWithId = teamLibrary.getTeamWithId(id);

        //THEN
        assertThat(teamWithId).isEqualTo(team1);
    }
}
