package dao.impl;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.UserDao;

import entities.Player;
import entities.User;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlets.HomeServlet;

import static dao.impl.DataSourceProvider.getDataSource;

public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(HomeServlet.class);

    public String getMotDePasseFromUtilisateur(String user) throws IOException {

        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            String sqlQuery = "SELECT mot_de_passe FROM utilisateur  WHERE nom_utilisateur=?";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1, user);
                try (ResultSet results = statement.executeQuery()) {
                    if (results.next()) {
                        String motDePasseHash = results.getString("mot_de_passe");
                        return motDePasseHash;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(String user, String password){
        String sqlQuery="UPDATE `utilisateur` SET mot_de_passe=?  WHERE nom_utilisateur=?;";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1,password);
                statement.setString(2,user);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //un void possible?
    //L'utilisateur qui s'inscrit est toujours considerer comme un joueur au début
    public User addUser(User user) throws UserAlreadyExistsException, IOException {
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "INSERT INTO utilisateur (nom_utilisateur, mot_de_passe, mail, role, score) VALUES (?, ?, ?, ?, 0);";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getMail());
                statement.setInt(4, user.getAdmin());
                statement.executeUpdate();

                LOG.debug("L'utilisateur {} est inscrit en tant que joueur",user.getName());

                return user;
            }
        } catch (SQLException e) {
            throw new UserAlreadyExistsException(user.getName(),e);
        }

    }

    //récupérer la liste des joueurs classé selon le score
    public List<Player> getAllJoueursOrderedByScore() throws IOException {

        List<Player> listRanking = new ArrayList<>();

        ResultSet resultSet = sendRequestSQL("SELECT nom_utilisateur,score FROM utilisateur  WHERE role=0 ORDER BY score DESC");

        try {
            while (resultSet.next()) {
                Player joueur = new Player(resultSet.getString("nom_utilisateur"), resultSet.getInt("score"));
                listRanking.add(joueur);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return listRanking;
    }

    private ResultSet sendRequestSQL(String sqlQuery1) throws IOException {
        try (Connection connection = getDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                 return statement.executeQuery(sqlQuery1);


            }
        } catch (SQLException e) {
            e.printStackTrace(); }//Il faut utilise LOGBACK
        return null;
    }

    //récupérer la liste des utilisateurs avec en premier les admins
    public List<User> getAllUsersOrderedByStatus() throws IOException {
        List<User> listUsers = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT nom_utilisateur,mot_de_passe,mail,role FROM utilisateur  ORDER BY role";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        User user = new User(results.getString("nom_utilisateur"),results.getString("mot_de_passe"),results.getString("mail"),results.getInt("role"));
                        listUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); }
        return listUsers;
    }

    public int getRole(String user) throws IOException {
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT role FROM utilisateur WHERE nom_utilisateur=?;";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1, user);
                try (ResultSet results = statement.executeQuery()) {
                    if (results.next()) {
                        return results.getInt("role");
                    }
                }
            }} catch (SQLException e) {
            e.printStackTrace();
        }
    return Integer.parseInt(null);//pour voir si ca marche a modifier
    }

    public void changeRole(String name) throws IOException {
        String sqlQuery = "UPDATE utilisateur SET role=? WHERE nom_utilisateur=?;";
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                if (getRole(name)==0){                       //Si l'utilisateur est un joueur, alors il passe admin
                    statement.setInt(1, 1);
                }
                else if (getRole(name)==1){                  //Si l'utilisateur est un admin, alors il passe joueur
                    statement.setInt(1,0);
                }
                statement.setString(2, name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String name) throws IOException {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM utilisateur WHERE nom_utilisateur=?;")){
                statement.setString(1,name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String name) throws UserNotFoundException, IOException {
        User user=null;
        String sqlQuery = "SELECT `nom_utilisateur`,`role` FROM `utilisateur` WHERE nom_utilisateur=?;";
        try(Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1,name);
                try (ResultSet result = statement.executeQuery()){
                    if (result.next()){
                        user = new User(
                                result.getString("nom_utilisateur"),
                                result.getInt("role"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            throw new UserNotFoundException(name,e);
        }
        return user;
    }

    //ajouter un point lorsque l'utilisateur a parie juste
    public void updateScore(String user) throws IOException {
        String sqlQuery="UPDATE `utilisateur` SET score=score+1  WHERE nom_utilisateur=?;";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1,user);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Player getPlayer(String name) throws IOException {
        Player player=null;
        String sqlQuery = "SELECT * FROM `utilisateur` WHERE nom_utilisateur=?;";
        try(Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1,name);
                try (ResultSet result = statement.executeQuery()){
                    if (result.next() && result.getInt("role")==0){
                        player = new Player(
                                result.getString("nom_utilisateur"),
                                result.getString("mot_de_passe"),
                                result.getString("mail"),
                                result.getInt("score"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

    public void addUrlProfil(String name, String url) throws IOException {
        String sqlQuery = "UPDATE `utilisateur` SET `photo_profil`=? WHERE `nom_utilisateur`=?;";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1,url);
                statement.setString(2,name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUrlProfil(String name) throws IOException {
        String url=null;
        String sqlQuery="SELECT `photo_profil` FROM `utilisateur` WHERE nom_utilisateur=?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()){
                    if (result.next()){
                        url = result.getString("photo_profil");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public List<User> searchUser(String filtre) throws IOException {
        List<User> users= new ArrayList<>();
        try(Connection connection = DataSourceProvider.getDataSource().getConnection()){
            String sqlQuery1 = "SELECT * FROM `utilisateur` WHERE nom_utilisateur=?;";
            searchAndAddUserToList(connection,sqlQuery1,filtre,users);
            String sqlQuery2 = "SELECT * FROM `utilisateur` WHERE nom_utilisateur LIKE ?; ";
            searchAndAddUserToList(connection,sqlQuery2,filtre+"_%",users);
            searchAndAddUserToList(connection,sqlQuery2,"%_"+filtre+"%",users);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Suppression des doublons
        users = deleteDouble(users);
        return users;
    }

    public void searchAndAddUserToList(Connection connection, String sqlQuery,String parameter,List<User> users) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            statement.setString(1,parameter);
            try (ResultSet result = statement.executeQuery()){
                while (result.next()){
                    User user = new User(
                            result.getString("nom_utilisateur"),
                            result.getInt("role"));
                    users.add(user);
                }
            }
        }
    }

    public List<User> deleteDouble(List<User> users){
        for (int i=0;i<(users.size()-1);i++){
            for(int j=(i+1);j<users.size();j++){
                if (users.get(i).getName().equals(users.get(j).getName())){
                    users.remove(j);
                }
            }
        }
        return users;
    }

    public List<User> searchUserWithRole(int role){
        List<User> listUsers = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT nom_utilisateur,role FROM utilisateur WHERE role=?;";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setInt(1, role);
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        User user = new User(results.getString("nom_utilisateur"),results.getInt("role"));
                        listUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); }
        return listUsers;
    }

}


