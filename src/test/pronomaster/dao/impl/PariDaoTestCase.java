package pronomaster.dao.impl;

import dao.PariDao;
import dao.impl.DataSourceProvider;
import dao.impl.PariDaoImpl;
import entities.Match;
import entities.Pari;
import entities.Team;
import exceptions.PariAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class PariDaoTestCase {

    PariDao pariDao = new PariDaoImpl();

    @Before
    public void init() throws Exception{
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
                Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM `parie`");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (1,1,2,3,'Lucie')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (2,4,4,1,'Lucie')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (3,8,3,2,'Lucie')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (4,1,1,3,'Philippe')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (5,8,0,2,'Emma')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (6,1,1,2,'Jacques')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (7,4,3,0,'Emma')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (8,8,1,1,'Philippe')");
            stmt.executeUpdate("INSERT INTO `parie`(`id_parie`,`id_match`,`but_in`,`but_out`,`id_utilisateur`) VALUES (9,3,3,1,'Emma')");

        }
    }



    @Test
    public void shouldListParisUtilisateur() throws IOException {
        //GIVEN
        String nom = "Lucie";

        //WHEN
        List<Pari> listOfUserParis = pariDao.listUnPassedUserBets(nom);

        //THEN
        assertThat(listOfUserParis).hasSize(3);
        assertThat(listOfUserParis).extracting("idPari","match.idMatch","butIn","butOut","idUser").containsOnly(
                tuple(1,1,2,3,"Lucie"),
                tuple(2,4,4,1,"Lucie"),
                tuple(3,8,3,2,"Lucie"));
    }

    @Test
    public void shouldAddPari() throws PariAlreadyExistsException, IOException {
        //GIVEN
        String nom="Jacques";
        Team teamIn = new Team(5,"Montpellier");
        Team teamOut = new Team(2,"Marseille");
        Match matchOfTheBet = new Match(4,teamIn,teamOut, LocalDate.of(2020,12,7));
        Pari pariToAdd = new Pari(9,matchOfTheBet,3,2,nom);

        //WHEN
        Pari addedPari = pariDao.addPari(pariToAdd);

        //THEN
        assertThat(addedPari).isNotNull();
        assertThat(addedPari.getIdPari()).isNotNull();
        assertThat(addedPari.getMatch().getIdMatch()).isEqualTo(4);
        assertThat(addedPari.getMatch().getTeamIn().getIdEquipe()).isEqualTo(5);
        assertThat(addedPari.getMatch().getTeamIn().getName()).isEqualTo("Montpellier");
        assertThat(addedPari.getMatch().getTeamOut().getIdEquipe()).isEqualTo(2);
        assertThat(addedPari.getMatch().getTeamOut().getName()).isEqualTo("Marseille");
        assertThat(addedPari.getMatch().getDate()).isEqualTo(LocalDate.of(2020, 12, 7));
        assertThat(addedPari.getButIn()).isEqualTo(3);
        assertThat(addedPari.getButOut()).isEqualTo(2);
        assertThat(addedPari.getIdUser()).isEqualTo("Jacques");
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `parie` WHERE id_parie=?")){
            stmt.setInt(1, addedPari.getIdPari());
            try (ResultSet rs = stmt.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("id_parie")).isEqualTo(addedPari.getIdPari());
                assertThat(rs.getInt("id_match")).isEqualTo(4);
                assertThat(rs.getInt("but_in")).isEqualTo(3);
                assertThat(rs.getInt("but_out")).isEqualTo(2);
                assertThat(rs.getString("id_utilisateur")).isEqualTo(nom);
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldListAllParisByIdMatch() throws IOException {

        //GIVEN
        int idMatch = 4;

        //WHEN
        List<Pari> listPari = pariDao.listAllParisByIdMatch(idMatch);

        //THEN
        assertThat(listPari).hasSize(2);
        assertThat(listPari).extracting("idPari","match.idMatch","butIn","butOut","idUser").containsOnly(
                tuple(2,4,4,1,"Lucie"),
                tuple(7,4,3,0,"Emma"));
    }

    @Test
    public void shouldUpdateResultat() throws IOException {

        //GIVEN
        int idPari = 3;
        boolean res = false;

        //WHEN
        pariDao.updateResultat(idPari,res);

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `parie` WHERE id_parie=?")){
            stmt.setInt(1, idPari);
            try (ResultSet rs = stmt.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getBoolean("resultat")).isEqualTo(res);
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldListParisPassedMatchs() throws IOException {

        //GIVEN
        String nom="Emma";

        //WHEN
        List<Pari> listParis = pariDao.listParisPassedMatchs(nom);

        //THEN
        assertThat(listParis).hasSize(1);
        assertThat(listParis).extracting("idPari","match.idMatch","butIn","butOut","idUser").containsOnly(
                tuple(9,3,3,1,"Emma"));

    }


}
