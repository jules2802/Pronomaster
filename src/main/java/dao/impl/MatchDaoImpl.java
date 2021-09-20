package dao.impl;

import dao.MatchDao;
import entities.Team;
import entities.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import exceptions.MatchAlreadyExistsException;
import exceptions.MatchNotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dao.impl.DataSourceProvider.getDataSource;

import java.time.LocalDate;

public class MatchDaoImpl implements MatchDao {

    private static final Logger LOG = LoggerFactory.getLogger(MatchDaoImpl.class);
    String errormessage = "";


    //Ajout d'un match
    public Match addMatch(Match match) throws MatchAlreadyExistsException, IOException {

        String sqlQuery = "INSERT INTO `match`(equipe_in, equipe_out, date) VALUES (?, ?, ?);";  //Attention, les équipe sont sous la forme d'un id
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, match.getTeamIn().getIdEquipe());
                statement.setInt(2, match.getTeamOut().getIdEquipe());
                statement.setDate(3, Date.valueOf(match.getDate().toString()));
                statement.executeUpdate();
                try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                    if (generatedKeys.next()){
                        Match addedMatch = new Match(generatedKeys.getInt(1),match.getTeamIn(), match.getTeamOut(),match.getDate());
                        LOG.debug("Instanciation des parametres du nouveau match : {}", addedMatch);
                        return addedMatch;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }

    //Récupération des matchs dont la date est passée est dont le score n'est pas encore rentré
    @Override
    public List<Match> listPassedMatchsWithoutScore() throws IOException {

        List<Match> listMatchs = new ArrayList<>();
        LocalDate ajd = LocalDate.now();
        LOG.info("Creation d'une liste de match passés sans score");
        String sqlQuery = "SELECT m.*, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out FROM `match` m JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe WHERE `date`<? AND `but_in` IS NULL ORDER BY `date`";
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setDate(1, Date.valueOf(ajd.toString()));
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
                                results.getDate("date").toLocalDate());
                        listMatchs.add(match);
                        LOG.debug("Ajout dans la liste d'un nouveau match passé : {}", listMatchs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("la connexion ne s'est pas effectuee",e);}

        return listMatchs;

    }

    //Récupération des matchs dont la date est passée et dont le score est déjà rentré
    @Override
    public List<Match> listPassedMatchsWithScore() throws IOException {

        List<Match> listMatchs = new ArrayList<>();
        LocalDate ajd = LocalDate.now();
        LOG.info("Creation d'une liste de match passés avec score");
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT m.*, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out FROM `match` m JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe WHERE date<? AND but_in IS NOT NULL ORDER BY `date`";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setDate(1, Date.valueOf(ajd.toString()));
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
                        listMatchs.add(match);
                        LOG.debug("Ajout dans la liste d'un nouveau match passé : {}", listMatchs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("la connexion ne s'est pas effectuee",e);}

        return listMatchs;
    }

    //Récupération des trois derniers matchs dont la date est passée et dont le score est déjà rentré
    @Override
    public List<Match> lastThreeMatchesWithScore() throws IOException {

        List<Match> listMatchs = new ArrayList<>();
        LocalDate ajd = LocalDate.now();
        LOG.info("Creation d'une liste des trois derniers match passés avec score");
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT m.*, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out FROM `match` m JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe WHERE date<? AND but_in IS NOT NULL ORDER BY `date`";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setDate(1, Date.valueOf(ajd.toString()));
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                            if(listMatchs.size()<3){
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
                            listMatchs.add(match);
                            LOG.debug("Ajout dans la liste d'un nouveau match passé : {}", listMatchs);
                        }

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("la connexion ne s'est pas effectuee",e);}

        return listMatchs;
    }



    //Récupération des matchs dont la date n'est pas passée
    @Override
    public List<Match> listUnpassedMatchs() throws IOException {

        List<Match> listMatchs = new ArrayList<>();
        LocalDate ajd = LocalDate.now();
        LOG.info("Creation d'une liste  match non passés");
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT m.*, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out FROM `match` m JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe WHERE date>? ORDER BY `date";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setDate(1, Date.valueOf(ajd.toString()));
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
                                results.getDate("date").toLocalDate());
                        listMatchs.add(match);
                        LOG.debug("Ajout dans la liste d'un nouveau match non passé : {}", listMatchs);
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("la connexion ne s'est pas effectuee",e);}

        return listMatchs;
    }

    //Rentrer les scores (but_in et but_out) dans un match existant
    @Override
    public void updateScore(int id_match, int goal_in, int goal_out) throws IOException {
        String sqlQuery="UPDATE `match` SET but_in=?,but_out=? WHERE id_match=?;";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setInt(1,goal_in);
                statement.setInt(2,goal_out);
                statement.setInt(3,id_match);
                statement.executeUpdate();
                LOG.info("Mise à jour du score");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("la connexion ne s'est pas effectuee",e);
    }
    }

    //recuperer un match grace à son identifiant
    @Override
    public Match getMatchById(int idMatch) throws MatchNotFoundException {
        Match match=null;
        LOG.info("Recuperation d'un match grace a son identifiant");
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT m.*, e_in.`nom_equipe` AS nom_equipe_in, e_out.`nom_equipe` AS nom_equipe_out FROM `match` m JOIN `equipe` e_in ON m.equipe_in=e_in.id_equipe JOIN `equipe` e_out ON m.equipe_out=e_out.id_equipe WHERE id_match=?";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setInt(1,idMatch);
                try (ResultSet results = statement.executeQuery()) {
                    if (results.next()) {
                        Team teamIn = new Team(
                                results.getInt("equipe_in"),
                                results.getString("nom_equipe_in"));
                        Team teamOut = new Team(
                                results.getInt("equipe_out"),
                                results.getString("nom_equipe_out"));
                        match = new Match(
                                results.getInt("id_match"),
                                teamIn,
                                teamOut,
                                results.getInt("but_in"),
                                results.getInt("but_out"),
                                results.getDate("date").toLocalDate());
                        return match;
                    }
                }
            }
        } catch (SQLException e) {
            throw new MatchNotFoundException(idMatch,e);
        }
        return match;
    }




    //Récupération des matchs pour lesquels le joueur n'a pas encore parié
    public List<Match> listUnbetMatchs(String userName){
        List<Match> listOfUnbetMatchs = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LOG.info("Creation d'une liste de matches non parié");
        String sqlQuery="SELECT e_in.id_equipe AS equipe_in,e_out.id_equipe AS equipe_out,m.id_match AS id_match,e_in.nom_equipe AS nom_equipe_in,e_out.nom_equipe AS nom_equipe_out,m.date FROM `match` m JOIN equipe e_in ON m.equipe_in=e_in.id_equipe JOIN equipe e_out ON m.equipe_out=e_out.id_equipe WHERE m.id_match NOT IN (SELECT m.id_match FROM`parie` p JOIN `match` m ON p.id_match=m.id_match WHERE p.id_utilisateur=?) AND m.date>? ORDER BY date;";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()){
            try (PreparedStatement statement=connection.prepareStatement(sqlQuery)){
                statement.setString(1,userName);
                statement.setDate(2,Date.valueOf(now));
                try(ResultSet results = statement.executeQuery()){
                    while (results.next()){
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
                                results.getDate("date").toLocalDate());
                        listOfUnbetMatchs.add(match);
                        LOG.debug("Ajout dans la liste d'un match non parié : {}",listOfUnbetMatchs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("la connexion ne s'est pas effectuee",e);
        }
        return listOfUnbetMatchs;
    }
}


