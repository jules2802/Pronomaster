package managers;


import dao.MatchDao;
import dao.impl.MatchDaoImpl;
import entities.Match;
import exceptions.MatchAlreadyExistsException;
import exceptions.MatchNotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MatchLibrary {

	//Création d'un singleton:tjrs la meme instance
	private static class MatchLibraryHolder {
		private final static MatchLibrary instance = new MatchLibrary();
	}

	private MatchDao matchDao = new MatchDaoImpl();

	public static MatchLibrary getInstance() {
		return MatchLibraryHolder.instance;
	}


	private MatchLibrary() {
	}

	public Match addMatch(Match match) throws MatchAlreadyExistsException, IOException {return this.matchDao.addMatch(match);}

	public List<Match> listPassedMatchsWithoutScore() throws IOException {return matchDao.listPassedMatchsWithoutScore();}

	public List<Match> listPassedMatchsWithScore() throws IOException {return matchDao.listPassedMatchsWithScore();}

	public List<Match> listMatchUnpassedMatchs() throws IOException {return matchDao.listUnpassedMatchs();}

	public void updateScore(int id_match, int but_in, int but_out) throws IOException {matchDao.updateScore(id_match, but_in, but_out);}

	public Match getMatchById(int idMatch) throws MatchNotFoundException, IOException {return matchDao.getMatchById(idMatch);}

	public List<Match> listUnbetMatchs(String userName) throws IOException {return  matchDao.listUnbetMatchs(userName);}

	public List<Match> lastThreeMatchesWithScore() throws IOException {return  matchDao.lastThreeMatchesWithScore();}
}
