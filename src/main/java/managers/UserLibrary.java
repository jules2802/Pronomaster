package managers;


import dao.UserDao;
import dao.impl.UserDaoImpl;
import entities.Player;
import entities.User;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import utils.MotDePasseUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserLibrary {

    //Création d'un singleton:tjrs la meme instance
    private static class UtilisateurLibraryHolder {

        private final static UserLibrary instance = new UserLibrary();
    }

    private UserDao userDao = new UserDaoImpl();

    public static UserLibrary getInstance() {

        return UtilisateurLibraryHolder.instance;
    }


    public boolean validerMotDePasse(String utilisateur, String mdp) throws IOException {
        String motDePasseHash = null;
        try {
            motDePasseHash = userDao.getMotDePasseFromUtilisateur(utilisateur);
            return MotDePasseUtils.validerMotDePasse(mdp, motDePasseHash);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User addUser(User user1) throws UserAlreadyExistsException, IOException {
        return userDao.addUser(user1);
    }

    public List<User> getAllUsersOrderedByStatus() throws IOException {
        return userDao.getAllUsersOrderedByStatus();
    }

    public int getRole(String user) throws IOException {
        return userDao.getRole(user);
    }

    public List<Player> getAllUsersOrderedByScore() throws IOException {
        return userDao.getAllJoueursOrderedByScore();
    }

    public void changeRole(String name) throws IOException {
        userDao.changeRole(name);
    }

    public void deleteUser(String name) throws IOException {
        userDao.deleteUser(name);
    }

    public User getUser(String name) throws UserNotFoundException, IOException { return userDao.getUser(name); }
    public void updateScore(String user) throws IOException {
        userDao.updateScore(user);}

    public Player getPlayer(String name) throws IOException {return userDao.getPlayer(name);}

    public void addUrlProfil(String name,String url) throws IOException {
        userDao.addUrlProfil(name,url);}

    public String getUrlProfil(String name) throws IOException {return userDao.getUrlProfil(name);}

    public void updatePassword(String user, String password) throws IOException {userDao.updatePassword(user,password);}

    public List<User> searchUser(String filtre) throws IOException {return userDao.searchUser(filtre);}

    public List<User> searchUserWithRole(int role){
        return userDao.searchUserWithRole(role);
    }

}
