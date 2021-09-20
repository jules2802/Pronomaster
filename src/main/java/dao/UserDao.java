package dao;

import entities.Player;
import entities.User;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {


    String getMotDePasseFromUtilisateur(String user) throws SQLException, IOException;
    User addUser(User user) throws UserAlreadyExistsException, IOException;
    void updatePassword(String user, String password) throws IOException;
    int getRole(String user) throws IOException;
    List<User> getAllUsersOrderedByStatus() throws IOException;
    List<Player> getAllJoueursOrderedByScore() throws IOException;
    void changeRole(String name) throws IOException;
    void deleteUser(String name) throws IOException;
    User getUser(String name) throws UserNotFoundException, IOException;
    Player getPlayer(String name) throws IOException;
    void addUrlProfil(String nom, String url) throws IOException;
    String getUrlProfil(String name) throws IOException;
    void updateScore(String user) throws IOException;
    List<User> searchUser(String filtre) throws IOException;
    List<User> searchUserWithRole(int role);
}
