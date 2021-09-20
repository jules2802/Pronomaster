package pronomaster.dao.impl;

import dao.MatchDao;
import dao.impl.DataSourceProvider;
import dao.impl.MatchDaoImpl;
import entities.Match;
import entities.Team;
import exceptions.MatchAlreadyExistsException;
import exceptions.MatchNotFoundException;
import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class MatchDaoTestCase {

    private MatchDao matchDao = new MatchDaoImpl();

    @Before
    public void init() throws Exception{
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()){
            stmt.executeUpdate("DELETE FROM `match`");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (1,2,3,null,null,'2020-11-22')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (2,1,4,null,null,'2018-11-11')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (3,4,3,4,1,'2018-11-02')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (4,5,2,null,null,'2020-12-07')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (5,3,1,1,2,'2018-11-18')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (6,1,2,null,null,'2018-11-08')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (7,1,5,2,2,'2018-11-13')");
            stmt.executeUpdate("INSERT INTO `match`(`id_match`,`equipe_in`,`equipe_out`,`but_in`,`but_out`,`date`) VALUES (8,2,4,null,null,'2021-12-12')");
        }
    }

    @Test
    public void shouldAddMatch() throws ParseException, MatchAlreadyExistsException, IOException {
        //GIVEN
        Team teamIn = new Team(1,"Paris");
        Team teamOut = new Team(2,"Marseille");
        LocalDate date = LocalDate.of(2018, 12, 1);
        Match matchToAdd=new Match(teamIn,teamOut,date);

        //WHEN
        Match addedMatch = matchDao.addMatch(matchToAdd);

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `match` WHERE id_match=?")){
                 stmt.setInt(1, addedMatch.getIdMatch());
            try (ResultSet rs = stmt.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("id_match")).isEqualTo(addedMatch.getIdMatch());
                assertThat(rs.getInt("equipe_in")).isEqualTo(1);
                assertThat(rs.getInt("equipe_out")).isEqualTo(2);
                assertThat(rs.getInt("but_in")).isEqualTo(0);
                assertThat(rs.getInt("but_out")).isEqualTo(0);
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(expected=ParseException.class)
    public void shouldNotAddMatch() throws ParseException, MatchAlreadyExistsException, IOException {
        //GIVEN
        Team teamIn = new Team(1,"Paris");
        Team teamOut = new Team(2,"Marseille");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateAsDate = formatter.parse("");
        LocalDate date = dateAsDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Match matchToAdd=new Match(teamIn,teamOut,date);

        //WHEN
        matchDao.addMatch(matchToAdd);

        //THEN
        fail();
    }

    @Test
    public void shouldListPassedMatchsWithoutScore() throws IOException {

        //WHEN
        List<Match> matches = matchDao.listPassedMatchsWithoutScore();

        //THEN
        assertThat(matches).hasSize(2);
        assertThat(matches).extracting("idMatch","teamIn.idEquipe","teamOut.idEquipe","date").containsOnly(
                tuple(2,1,4,LocalDate.of(2018, 11, 11)),
                tuple(6,1,2,LocalDate.of(2018, 11, 8))
        );
    }

    @Test
    public void shouldListPassedMatchsWithScore() throws IOException {

        //WHEN
        List<Match> matches = matchDao.listPassedMatchsWithScore();

        //THEN
        assertThat(matches).hasSize(3);
        assertThat(matches).extracting("idMatch","teamIn.idEquipe","teamOut.idEquipe","butIn","butOut","date").containsOnly(
                tuple(3,4,3,4,1,LocalDate.of(2018, 11, 2)),
                tuple(5,3,1,1,2,LocalDate.of(2018, 11, 18)),
                tuple(7,1,5,2,2,LocalDate.of(2018, 11, 13))

        );
    }

    @Test
    public void shouldListLastThreeMatchesWithScore() throws IOException {

        //WHEN
        List<Match> matches = matchDao.lastThreeMatchesWithScore();

        //WHEN
        assertThat(matches).hasSize(3);
        assertThat(matches).extracting("idMatch","teamIn.idEquipe","teamOut.idEquipe","butIn","butOut","date").containsOnly(
                tuple(3,4,3,4,1,LocalDate.of(2018, 11, 2)),
                tuple(7,1,5,2,2,LocalDate.of(2018, 11, 13)),
                tuple(5,3,1,1,2,LocalDate.of(2018, 11, 18))
        );
    }

    @Test
    public void shouldListUnpassedMatchs() throws IOException {
        //WHEN
        List<Match> unpassedMatches = matchDao.listUnpassedMatchs();

        //THEN
        assertThat(unpassedMatches).hasSize(3);
        assertThat(unpassedMatches).extracting("idMatch","teamIn.idEquipe","teamOut.idEquipe","date").containsOnly(
                tuple(1,2,3,LocalDate.of(2020, 11, 22)),
                tuple(4,5,2,LocalDate.of(2020, 12, 7)),
                tuple(8,2,4,LocalDate.of(2021, 12, 12))
        );
    }

    @Test
    public void shouldUpdateScore() throws IOException {
        //GIVEN
        int butIn=4;
        int butOut=3;

        //WHEN
        matchDao.updateScore(2, butIn, butOut);

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM `match` WHERE id_match=2")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("id_match")).isEqualTo(2);
                assertThat(rs.getInt("equipe_in")).isEqualTo(1);
                assertThat(rs.getInt("equipe_out")).isEqualTo(4);
                assertThat(rs.getInt("but_in")).isEqualTo(4);
                assertThat(rs.getInt("but_out")).isEqualTo(3);
                assertThat(rs.getDate("date")).isEqualTo("2018-11-11");
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldgetMatchById() throws MatchNotFoundException, IOException {
        //GIVEN
        int id=3;
        Team teamIn = new Team(4,"Nantes");
        Team teamOut = new Team(3,"Lille");
        Match matchToGet=new Match(3,teamIn,teamOut,4,1,LocalDate.of(2018, 11, 2));

        //WHEN
        Match match = matchDao.getMatchById(id);

        //THEN
        assertThat(match).isNotNull();
        assertThat(match.getIdMatch()).isEqualTo(matchToGet.getIdMatch());
        assertThat(match.getTeamIn().getIdEquipe()).isEqualTo(matchToGet.getTeamIn().getIdEquipe());
        assertThat(match.getTeamIn().getName()).isEqualTo(matchToGet.getTeamIn().getName());
        assertThat(match.getTeamOut().getIdEquipe()).isEqualTo(matchToGet.getTeamOut().getIdEquipe());
        assertThat(match.getTeamOut().getName()).isEqualTo(matchToGet.getTeamOut().getName());
        assertThat(match.getButIn()).isEqualTo(matchToGet.getButIn());
        assertThat(match.getButOut()).isEqualTo(matchToGet.getButOut());
        assertThat(match.getDate()).isEqualTo(matchToGet.getDate());
    }

    @Test
    public void shouldListUnbetMatchs() throws IOException {
        //GIVEN
        String name = "Lucie";

        //WHEN
        List<Match> listOfUnbetMatchs = matchDao.listUnbetMatchs(name);

        //THEN : grâce à la fonction placée en @Before, on sait que l'utilisatrice "Lucie" n'a jamais parié donc cette liste devrait contenir tous les matchs qui ne sont pas encore passés
        assertThat(listOfUnbetMatchs).hasSize(3);
        assertThat(listOfUnbetMatchs).extracting("idMatch","teamIn.IdEquipe","teamIn.name","teamOut.idEquipe","teamOut.name","date").containsOnly(
                tuple(1,2,"Marseille",3,"Lille",LocalDate.of(2020,11,22)),
                tuple(4,5,"Montpellier",2,"Marseille",LocalDate.of(2020, 12, 7)),
                tuple(8,2,"Marseille",4,"Nantes",LocalDate.of(2021, 12, 12)));
    }

    @Test
    public void shouldListUnbetMatchsNew() throws IOException {
        //GIVEN : On simule la réalisation d'un pari par l'utilisateur "Lucie"
        String name="Lucie";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()){
            stmt.executeUpdate("DELETE FROM `parie`");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (1,1,2,3,'Lucie')");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //WHEN
        List<Match> listOfUnbetMatchs = matchDao.listUnbetMatchs(name);

        //THEN : vu que "Lucie" a parié à un trois matchs, la liste obtenue devrait être modifiée en conséquence
        assertThat(listOfUnbetMatchs).hasSize(2);
        assertThat(listOfUnbetMatchs).extracting("idMatch","teamIn.IdEquipe","teamIn.name","teamOut.idEquipe","teamOut.name","date").containsOnly(
                tuple(4,5,"Montpellier",2,"Marseille",LocalDate.of(2020, 12, 7)),
                tuple(8,2,"Marseille",4,"Nantes",LocalDate.of(2021, 12, 12)));

    }

    @Test(expected = AssertionFailedError.class)
    public void shouldNotListUnbetMatchs() throws IOException {
        //GIVEN
        String nonExistingName="Sofiane";

        //WHEN
        matchDao.listUnbetMatchs(nonExistingName);

        //THEN
        fail();
    }


}
