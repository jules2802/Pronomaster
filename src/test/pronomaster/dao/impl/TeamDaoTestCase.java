package pronomaster.dao.impl;


import dao.TeamDao;
import dao.impl.TeamDaoImpl;
import entities.Team;
import exceptions.TeamNotFoundException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class TeamDaoTestCase {

    TeamDao teamDao = new TeamDaoImpl();

    @Test
    public void shouldGetAllTeams() throws IOException {

        //WHEN
        List<Team> listOfTeams = teamDao.getAllTeams();

        //THEN
        assertThat(listOfTeams).hasSize(8);
        assertThat(listOfTeams).extracting("idEquipe","name").containsOnly(
                tuple(1,"Paris"),
                tuple(2,"Marseille"),
                tuple(3,"Lille"),
                tuple(4,"Nantes"),
                tuple(5,"Montpellier"),
                tuple(6,"Lyon"),
                tuple(7,"Saint-Etienne"),
                tuple(8,"Nice"));
    }

    @Test
    public void shouldgGetTeamWithId() throws TeamNotFoundException, IOException {
        //GIVEN
        int teamId=3;

        //WHEN
        Team team = teamDao.getTeamWithId(teamId);

        //THEN
        assertThat(team).isNotNull();
        assertThat(team.getIdEquipe()).isEqualTo(3);
        assertThat(team.getName()).isEqualTo("Lille");

        //THEN

    }
}
