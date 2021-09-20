package dao.impl;


import dao.PariDao;
import entities.Match;
import entities.Pari;
import entities.Team;
import exceptions.PariAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlets.HistoricOfBetServlet;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static dao.impl.DataSourceProvider.getDataSource;

public class PariDaoImpl implements PariDao {

    private static final Logger LOG = LoggerFactory.getLogger(PariDaoImpl.class);

    //Récupération des paris pour lesquels l'utilisateur a parié
    @Override
    public List<Pari> listUnPassedUserBets(String userName) throws IOException {
        String sqlQuery = "SELECT m.date,e_in.id_equipe AS id_equipe_in,e_out.id_equipe AS id_equipe_out,m.id_match,p.id_parie,p.but_in,p.but_out,u.nom_utilisateur, e_in.nom_equipe AS nom_equipe_in,e_out.nom_equipe AS nom_equipe_out FROM `parie` p JOIN `match` m ON p.id_match=m.id_match JOIN `utilisateur` u ON u.nom_utilisateur=p.id_utilisateur JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe WHERE p.id_utilisateur= ? AND m.date>?;";
        List<Pari> paris = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1,userName);
                statement.setDate(2,Date.valueOf(LocalDate.now().toString()));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        Team equipeIn = new Team(
                                resultSet.getInt("id_equipe_in"),
                                resultSet.getString("nom_equipe_in"));
                        Team equipeOut= new Team(
                                resultSet.getInt("id_equipe_out"),
                                resultSet.getString("nom_equipe_out"));
                        Match match= new Match(
                                resultSet.getInt("id_match"),
                                equipeIn,
                                equipeOut,
                                resultSet.getDate("date").toLocalDate());
                        paris.add(new Pari(resultSet.getInt("id_parie"),
                                match,
                                resultSet.getInt("but_in"),
                                resultSet.getInt("but_out"),
                                resultSet.getString("nom_utilisateur")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paris;
    }

    //Création d'un pari par l'utilisateur
    @Override
    public Pari addPari(Pari pari) throws PariAlreadyExistsException, IOException {
        Pari addedPari = null;
        String sqlQuery = "INSERT INTO parie (id_match, but_in, but_out, id_utilisateur) VALUES (?,?,?,?)";
        try (Connection connection= DataSourceProvider.getDataSource().getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS)){
                statement.setInt(1,pari.getMatch().getIdMatch());
                statement.setInt(2,pari.getButIn());
                statement.setInt(3,pari.getButOut());
                statement.setString(4,pari.getIdUser());
                statement.executeUpdate();

                try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                    if (generatedKeys.next()){
                        pari.setIdPari(generatedKeys.getInt(1));
                        addedPari=pari;
                    }
                }

            }
        } catch (SQLException e) {
            throw new PariAlreadyExistsException(pari.getIdPari(),e);
        }
        return addedPari;
    }

    //NON UTILISEE?
    public List<Pari> listAllParisByIdMatch(int idMatch) throws IOException {

        List<Pari> listParis = new ArrayList<>();

        String sqlQuery = "SELECT p.*,m.*, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out FROM `match` m JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe, `parie` p WHERE p.id_match=m.id_match AND p.id_match=?";
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setInt(1, idMatch);
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        Team equipeIn = new Team(
                                results.getInt("equipe_in"),
                                results.getString("nom_equipe_in"));
                        Team equipeOut= new Team(
                                results.getInt("equipe_out"),
                                results.getString("nom_equipe_out"));
                        Match match= new Match(
                                results.getInt("id_match"),
                                equipeIn,
                                equipeOut,
                                results.getInt("but_in"),
                                results.getInt("but_out"),
                                results.getDate("date").toLocalDate());

                        Pari pari= new Pari(
                                results.getInt("id_parie"),
                                match,
                                results.getInt("but_in"),
                                results.getInt("but_out"),
                                results.getString("id_utilisateur"));

                        listParis.add(pari);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); }

        return listParis;
    }

    //Mise à jour du score du joueur
    public void updateResultat(int idPari, boolean res) throws IOException {
        String sqlQuery="UPDATE `parie` SET resultat=?  WHERE id_parie=?;";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setBoolean(1,res);
                statement.setInt(2,idPari);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Récupération des paris pour les matchs déjà passés
    @Override
    public List<Pari> listParisPassedMatchs(String idUser) throws IOException {
        List<Pari> listParisPassedMatchs = new ArrayList<>();
        LocalDate ajd = LocalDate.now();
        String sqlQuery = "SELECT p.resultat,m.date,p.id_parie,p.but_in AS but_in_pari,p.but_out AS but_out_pari,e_in.id_equipe AS equipe_in,e_out.id_equipe AS equipe_out, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out,p.id_match FROM`match` m JOIN equipe e_in ON m.equipe_in=e_in.id_equipe JOIN equipe e_out ON m.equipe_out=e_out.id_equipe, parie p WHERE p.id_match=m.id_match AND p.id_utilisateur=? AND date<? AND p.resultat IS NOT NULL";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1, idUser);
                statement.setDate(2, Date.valueOf(ajd.toString()));
                addPariFromStatement(statement,listParisPassedMatchs,idUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listParisPassedMatchs;
    }

    public void addPariFromStatement(PreparedStatement statement,List<Pari> listParis, String idUser){
        try (ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                Team teamIn = new Team(
                        results.getInt("equipe_in"),
                        results.getString("nom_equipe_in"));
                Team teamOut = new Team(
                        results.getInt("equipe_out"),
                        results.getString("nom_equipe_out"));
                Match match = new Match(
                        results.getInt("id_match"),
                        teamIn,
                        teamOut,
                        results.getInt("but_in"),
                        results.getInt("but_out"),
                        results.getDate("date").toLocalDate());
                Pari pari = new Pari(
                        results.getInt("id_parie"),
                        match,
                        results.getInt("but_in_pari"),
                        results.getInt("but_out_pari"),
                        idUser,
                        results.getInt("resultat"));

                listParis.add(pari);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public List<Pari> searchBet(String idUser,String filtreTeam,String filtreDate,int filtreResult) throws IOException {
        List<Pari> listParisPassedMatchs = new ArrayList<>();
        String sqlQuery = "SELECT p.resultat,m.date,p.id_parie,p.but_in AS but_in_pari,p.but_out AS but_out_pari,e_in.id_equipe AS equipe_in,e_out.id_equipe AS equipe_out, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out,p.id_match FROM`match` m JOIN equipe e_in ON m.equipe_in=e_in.id_equipe JOIN equipe e_out ON m.equipe_out=e_out.id_equipe, parie p WHERE p.id_match=m.id_match AND p.id_utilisateur=? AND p.resultat IS NOT NULL";
        String parameter;
        if (filtreTeam.equals("null")==false){
            if (filtreDate.equals("")==false){
                if (filtreResult!=2){
                    LOG.info("Les champs 'équipe','date' et 'résultat' ont été saisis");
                    parameter=" AND (e_in.`nom_equipe`=? OR e_out.`nom_equipe`=?) AND date=? AND p.resultat=?;";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setString(2, filtreTeam);
                            statement.setString(3, filtreTeam);
                            statement.setString(4, filtreDate);
                            statement.setInt(5, filtreResult);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    LOG.info("Les champs 'équipe' et 'date' ont été saisis");
                    parameter=" AND (e_in.`nom_equipe`=? OR e_out.`nom_equipe`=?) AND date=?";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setString(2, filtreTeam);
                            statement.setString(3, filtreTeam);
                            statement.setString(4, filtreDate);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else{
                if (filtreResult!=2){
                    LOG.info("Les champs 'équipe' et 'résultat' ont été saisis");
                    parameter=" AND (e_in.`nom_equipe`=? OR e_out.`nom_equipe`=?) AND p.resultat=?;";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setString(2, filtreTeam);
                            statement.setString(3, filtreTeam);
                            statement.setInt(4, filtreResult);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    LOG.info("Le champ 'équipe' a été saisi");
                    parameter=" AND (e_in.`nom_equipe`=? OR e_out.`nom_equipe`=?)";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setString(2, filtreTeam);
                            statement.setString(3, filtreTeam);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            if (filtreDate.equals("")==false){
                if (filtreResult!=2){
                    LOG.info("Les champs 'date' et 'résultat' ont été saisis");
                    parameter=" AND date=? AND p.resultat=?;";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setString(2, filtreDate);
                            statement.setInt(3, filtreResult);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    LOG.info("Le champ 'date' a été saisi");
                    parameter=" AND date=?";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setString(2, filtreDate);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else{
                if (filtreResult!=2){
                    LOG.info("Le champ 'résultat' a été saisi");
                    parameter=" AND p.resultat=?;";
                    sqlQuery = sqlQuery+parameter;
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            statement.setInt(2, filtreResult);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    LOG.info("Aucun champ n'a été saisi");
                    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                            statement.setString(1, idUser);
                            addPariFromStatement(statement,listParisPassedMatchs,idUser);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return listParisPassedMatchs;
    }

    public void deleteBet(String name, int idBet) {
        System.out.println(name);System.out.println(idBet);
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM `parie` WHERE `id_parie`=? AND`id_utilisateur`=?;")){
                statement.setInt(1,idBet);
                statement.setString(2,name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


