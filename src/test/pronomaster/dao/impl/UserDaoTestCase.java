package pronomaster.dao.impl;

import dao.UserDao;
import dao.impl.DataSourceProvider;
import dao.impl.UserDaoImpl;

import entities.Player;
import entities.User;

import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import utils.MotDePasseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoTestCase {

    @InjectMocks
    private UserDao userDao = new UserDaoImpl();

    @Mock
    private UserDao userDao1 = new UserDaoImpl();

    @Before
    public void init() throws Exception{
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
                Statement stmt = connection.createStatement()){
            stmt.executeUpdate("DELETE FROM utilisateur");
            stmt.executeUpdate("INSERT INTO `utilisateur`(`nom_utilisateur`,`mot_de_passe`,`mail`,`role`,`score`) VALUES ('Hugo','hugo','mail1',0,120)");
            stmt.executeUpdate("INSERT INTO `utilisateur`(`nom_utilisateur`,`mot_de_passe`,`mail`,`role`,`score`) VALUES ('Pierre','pierre','mail2',0,10)");
            stmt.executeUpdate("INSERT INTO `utilisateur`(`nom_utilisateur`,`mot_de_passe`,`mail`,`role`,`score`,`photo_profil`) VALUES ('Thomas','thomas','mail3',1,0,'/Users/paulmathon/Pictures/')");
        }
    }
    //Recontruction d'une base utilisateur importante
    @AfterClass
    public static void addLotOfUsersToDataBase() throws UserAlreadyExistsException, IOException {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM utilisateur");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User jules = new Player("Jules", MotDePasseUtils.genererMotDePasse("jules"), "jules");
        new UserDaoImpl().addUser(jules);
        User kanesha = new Player("Kanesha", MotDePasseUtils.genererMotDePasse("kanesha"), "kanesha");
        new UserDaoImpl().addUser(kanesha);
        User paul = new Player("Paul", MotDePasseUtils.genererMotDePasse("paul"), "paul");
        new UserDaoImpl().addUser(paul);
        User jacques = new Player("Jacques", MotDePasseUtils.genererMotDePasse("jacques"), "jacques");
        new UserDaoImpl().addUser(jacques);
        User philippe = new Player("Philippe", MotDePasseUtils.genererMotDePasse("philippe"), "philippe");
        new UserDaoImpl().addUser(philippe);
        User marie = new Player("Marie", MotDePasseUtils.genererMotDePasse("marie"), "marie");
        new UserDaoImpl().addUser(marie);
        User lucie = new Player("Lucie", MotDePasseUtils.genererMotDePasse("lucie"), "lucie");
        new UserDaoImpl().addUser(lucie);
        User emma = new Player("Emma", MotDePasseUtils.genererMotDePasse("emma"), "emma");
        new UserDaoImpl().addUser(emma);
        User hugo = new Player("Hugo", MotDePasseUtils.genererMotDePasse("hugo"), "hugo");
        new UserDaoImpl().addUser(hugo);
        User pierre = new Player("Pierre", MotDePasseUtils.genererMotDePasse("pierre"), "pierre");
        new UserDaoImpl().addUser(pierre);
        User thomas = new Player("Thomas", MotDePasseUtils.genererMotDePasse("thomas"), "thomas");
        new UserDaoImpl().addUser(thomas);
        User maxence = new Player("Maxence", MotDePasseUtils.genererMotDePasse("maxence"), "maxence");
        new UserDaoImpl().addUser(maxence);
        new UserDaoImpl().changeRole("Maxence");
    }

    @Test
    public void shouldgetMotDePasseFromUtilisateur() throws SQLException, IOException {
        //WHEN
        String mdp = userDao.getMotDePasseFromUtilisateur("Hugo");
        //THEN
        assertThat(mdp).isNotNull();
        assertThat(mdp).isEqualTo("hugo");
    }

    @Test
    public void shouldNotGetMotDePasseFromUtilisateur() throws SQLException, IOException {
        //WHEN
        String mdp = userDao.getMotDePasseFromUtilisateur("Aurélien");
        // THEN
        assertThat(mdp).isNull();
    }

    @Test
    public void shouldAddUser() throws Exception{
        //GIVEN
        User utilisateurToCreate = new User("nom1","motdepasse1","mail1", 0);
        //WHEN
        User utilisateurCreated = userDao.addUser(utilisateurToCreate);

        //THEN
        assertThat(utilisateurCreated).isNotNull();
        assertThat(utilisateurCreated.getName()).isEqualTo("nom1");
        assertThat(utilisateurCreated.getPassword()).isEqualTo("motdepasse1");
        assertThat(utilisateurCreated.getMail()).isEqualTo("mail1");
        assertThat(utilisateurCreated.getAdmin()).isEqualTo(0);

        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE nom_utilisateur = 'nom1'")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getString("nom_utilisateur")).isEqualTo("nom1");
                assertThat(rs.getString("mot_de_passe")).isEqualTo("motdepasse1");
                assertThat(rs.getString("mail")).isEqualTo("mail1");
                assertThat(rs.getInt("role")).isEqualTo(0);

                assertThat(rs.next()).isFalse();
            }
        }
    }

    @Test
    public void shouldGetAllJoueursOrderedByScore() throws IOException {

        //WHEN
        List<Player> joueurs = userDao.getAllJoueursOrderedByScore();

        //THEN
        assertThat(joueurs).hasSize(2);
        assertThat(joueurs).extracting("name","score").containsOnly(
                tuple("Hugo",120),
                tuple("Pierre",10)
        );
    }

    @Test
    public void shouldGetAllUsersOrderedByStatus() throws IOException {

        //WHEN
        List<User> users = userDao.getAllUsersOrderedByStatus();

        //THEN
        assertThat(users).hasSize(3);
        assertThat(users).extracting("name","admin").containsOnly(
                tuple("Hugo",0),
                tuple("Pierre",0),
                tuple("Thomas",1)
        );

    }

    @Test
    public void shouldGetRole() throws IOException {
        //WHEN
        int role = userDao.getRole("Hugo");

        //THEN
        assertThat(role).isEqualTo(0);
    }

    @Test(expected=NumberFormatException.class)
    public void shouldThrowNumberFormatException() throws IOException {
        //WHEN
        int role = userDao.getRole("Aurélien");

        //THEN
        fail();
    }

    @Test
    public void shouldChangeRole() throws IOException {

        //GIVEN
        Mockito.when(userDao1.getRole("Hugo")).thenReturn(0);

        //WHEN
        userDao.changeRole("Hugo");

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT role FROM utilisateur WHERE nom_utilisateur = 'Hugo'")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("role")).isEqualTo(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldDeleteUser() throws IOException {

        //WHEN
        userDao.deleteUser("Hugo");

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE nom_utilisateur = 'Hugo'")) {
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGetUser() throws UserNotFoundException, IOException {
        //GIVEN
        String nom ="Hugo";

        //WHEN
        User user = userDao.getUser(nom);

        //THEN
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Hugo");
        assertThat(user.getAdmin()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateScore() throws IOException {
        //GIVEN
        String user = "Hugo";

        //WHEN
        userDao.updateScore(user);

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT score FROM utilisateur WHERE nom_utilisateur = 'Hugo'")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("score")).isEqualTo(121);
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGetJoueur() throws IOException {
        //GIVEN
        String playerName = "Hugo";

        //WHEN
        Player player = userDao.getPlayer(playerName);

        //THEN
        assertThat(player).isNotNull();
        assertThat(player.getName()).isEqualTo("Hugo");
        assertThat(player.getMail()).isEqualTo("mail1");
        assertThat(player.getPassword()).isEqualTo("hugo");
        assertThat(player.getScore()).isEqualTo(120);
    }

    @Test
    public void shouldAddUrlProfil() throws IOException {
        //GIVEN
        String userName="Hugo";
        String url = "/Users/paulmathon/Pictures/";

        //WHEN
        userDao.addUrlProfil(userName, url);

        //THEN
        try (Connection connection = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT photo_profil FROM utilisateur WHERE nom_utilisateur = 'Hugo'")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getString("photo_profil")).isEqualTo("/Users/paulmathon/Pictures/");
                assertThat(rs.next()).isFalse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGetUrlProfil() throws IOException {
        //GIVEN
        String userName="Thomas";

        //WHEN
        String url = userDao.getUrlProfil(userName);

        //THEN
        assertThat(url).isEqualTo("/Users/paulmathon/Pictures/");
    }
}
