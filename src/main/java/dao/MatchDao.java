package dao;

import entities.Match;
import exceptions.MatchAlreadyExistsException;
import exceptions.MatchNotFoundException;

import java.io.IOException;

import java.util.List;

public interface MatchDao {

    Match addMatch(Match match) throws MatchAlreadyExistsException, IOException;
    List<Match> listUnpassedMatchs() throws IOException;
    List<Match> listPassedMatchsWithoutScore() throws IOException;
    List<Match> listPassedMatchsWithScore() throws IOException;
    void updateScore(int id_match, int but_in, int but_out) throws IOException;
    Match getMatchById(int idMatch) throws MatchNotFoundException, IOException;
    List<Match> listUnbetMatchs(String userName) throws IOException;
    List<Match> lastThreeMatchesWithScore() throws IOException;




}
