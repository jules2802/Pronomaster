package managers;

import dao.UserDao;

import entities.Player;
import entities.User;

import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import utils.MotDePasseUtils;

import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserLibraryTestCase {
    @InjectMocks
    private UserLibrary userLibrary = UserLibrary.getInstance();

    @Mock
    private UserDao userDao;

    @Mock
    private MotDePasseUtils motDePasseUtils ;

    @Test
    public void shouldValiderMotDePasse() throws SQLException, IOException {

        //GIVEN
        String user ="Hugo";
        String password ="hugo";
        String passwordHash= motDePasseUtils.genererMotDePasse(password);
        Mockito.when(userDao.getMotDePasseFromUtilisateur(user)).thenReturn(passwordHash);

        //WHEN
        Boolean result = userLibrary.validerMotDePasse(user, password);
        //THEN
        assertThat(result).isTrue();
    }


    @Test
    public void ShouldAddUser() throws UserAlreadyExistsException, IOException {
        //GIVEN
        String name="Thibault";
        String password="thibault";
        String mail="thibault.test@gmail.com";
        int admin=0;
        User user=new User(name,password,mail,admin);
        Mockito.when(userDao.addUser(user)).thenReturn(user);


        //WHEN
        User userCreated = userLibrary.addUser(user);

        //THEN
        assertThat(userCreated).isEqualTo(user);

    }

    @Test
    public void shouldGetAllUsersOrderedByStatus() throws IOException {
        //GIVEN
        User user1=new User("Hugo","hugo","mail1",0);
        User user2=new User("Pierre","pierre","mail2",0);
        User user3= new User("Thibault","thibault","mail3",1);
        List<User> usersList=new ArrayList<>();
        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);
        Mockito.when(userDao.getAllUsersOrderedByStatus()).thenReturn(usersList);

        //WHEN
        List<User> usersOrderedByStatus = userLibrary.getAllUsersOrderedByStatus();

        //THEN
        assertThat(usersOrderedByStatus).isEqualTo(usersList);
    }

    @Test
    public void shouldGetRole() throws IOException {
        //GIVEN
        String user="Hugo";
        int role=0;
        Mockito.when(userDao.getRole(user)).thenReturn(role);

        //WHEN
        int result=userLibrary.getRole(user);

        //THEN
        assertThat(result).isEqualTo(role);
    }

    @Test
    public void shouldGetAllUsersOrderedByScore() throws IOException {
        //GIVEN
        Player player1=new Player("Hugo","hugo","mail1",120);
        Player player2=new Player("Pierre","pierre","mail2",10);
        Player player3= new Player("Thibault","thibault","mail3",0);
        List<Player> playersList=new ArrayList<>();
        playersList.add(player1);
        playersList.add(player2);
        playersList.add(player3);
        Mockito.when(userDao.getAllJoueursOrderedByScore()).thenReturn(playersList);

        //WHEN
        List<Player> usersOrderedByScore = userLibrary.getAllUsersOrderedByScore();

        //THEN
        assertThat(usersOrderedByScore).isEqualTo(playersList);
    }

    @Test
    public void shouldChangeRole() throws IOException {
        //GIVEN
        String name="Hugo";

        //WHEN
        UserLibrary.getInstance().changeRole(name);

        //THEN
        Mockito.verify(userDao).changeRole(name);

    }

    @Test
    public void shouldDeleteUser() throws IOException {
        //GIVEN
        String name="Hugo";

        //WHEN
        userLibrary.deleteUser(name);

        //THEN
        Mockito.verify(userDao).deleteUser(name);
    }

    @Test
    public void shouldGetUser() throws UserNotFoundException, IOException {
        //GIVEN
        String name="Pierre";
        User user= new User("Pierre","pierre","mail2",0);
        Mockito.when(userDao.getUser(name)).thenReturn(user);

        //WHEN
        User userCreated = userLibrary.getUser(name);

        //THEN
        assertThat(userCreated).isEqualTo(user);
    }

    @Test
    public void shouldUpdateScore() throws IOException {
        //GIVEN
        String userName = "Thomas";

        //WHEN
        userLibrary.updateScore(userName);

        //THEN
        Mockito.verify(userDao).updateScore(userName);
    }

    @Test
    public void shouldGetPlayer() throws IOException {
        //GIVEN
        String name = "Thomas";
        Player player=new Player("Thomas","thomas","mail3",0);
        Mockito.when(userDao.getPlayer(name)).thenReturn(player);

        //WHEN
        Player playerFound = userLibrary.getPlayer(name);

        //THEN
        assertThat(playerFound).isEqualTo(player);
        
    }

    @Test
    public void shouldAddUrlProfil() throws IOException {
        //GIVEN
        String name="Thomas";
        String url="/webapp/images";


        //WHEN
        userLibrary.addUrlProfil(name,url);

        //THEN
        Mockito.verify(userDao).addUrlProfil(name,url);
    }

    @Test
    public void shouldGetUrlProfil() throws IOException {
        //GIVEN
        String name="Thomas";
        String urlProfile ="/webapp/images/Thomas";
        Mockito.when(userDao.getUrlProfil(name)).thenReturn(urlProfile);

        //WHEN
        String result=userLibrary.getUrlProfil(name);

        //THEN
        assertThat(result).isEqualTo(urlProfile);
    }

    @Test
    public void shouldUpdatePassword() throws IOException{
        //GIVEN
        String user="pierre";
        String password="pierre";

        //WHEN
        userLibrary.updatePassword(user,password);

        //THEN
        Mockito.verify(userDao).updatePassword(user,password);
    }

    @Test
    public void shouldSearchUser() throws IOException{
        //GIVEN
        String filtre="pierre";
        User user= new User(filtre);
        List<User> userList=new ArrayList<>();
        userList.add(user);
        Mockito.when(userDao.searchUser(filtre)).thenReturn(userList);

        //WHEN
        List<User> userFound=userLibrary.searchUser(filtre);

        //THEN
        assertThat(userFound).isEqualTo(userList);

    }

    @Test
    public void shouldSearchUserWithRole(){
        //GIVEN
        int role=0;
        User user=new User("pierre");
        List<User> researchList=new ArrayList<>();
        researchList.add(user);
        Mockito.when(userDao.searchUserWithRole(role)).thenReturn(researchList);

        //WHEN
        List<User> researchResult = userLibrary.searchUserWithRole(role);

        //THEN
        assertThat(researchResult).isEqualTo(researchList);
    }

}