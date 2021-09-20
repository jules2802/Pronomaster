package dao.impl;


import dao.TeamDao;
import entities.Match;
import entities.Player;
import entities.Team;
import exceptions.TeamNotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dao.impl.DataSourceProvider.getDataSource;

public class TeamDaoImpl implements TeamDao {

    public List<Team> getAllTeams() throws IOException {

        List<Team> listTeam = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT id_equipe,nom_equipe FROM equipe  ORDER BY id_equipe";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet results = statement.executeQuery(sqlQuery)) {
                    while (results.next()) {
                        Team team = new Team(results.getInt("id_equipe"),
                                results.getString("nom_equipe"));
                        listTeam.add(team);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTeam;
    }

    public Team getTeamWithId(int id) throws TeamNotFoundException, IOException {
        Team team = null;
        try (Connection connection = getDataSource().getConnection()) {
            String sqlQuery = "SELECT nom_equipe FROM equipe  WHERE id_equipe=?;";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setInt(1,id );
                try (ResultSet results = statement.executeQuery()) {
                    if (results.next()) {
                        String nom=results.getString("nom_equipe");
                        team = new Team(id,nom);
                    }
                }
            }
        } catch (SQLException e) {
            throw new TeamNotFoundException(id,e);
        }
        return team;
    }

}

