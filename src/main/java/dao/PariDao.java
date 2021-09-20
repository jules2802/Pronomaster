package dao;

import entities.Pari;
import exceptions.PariAlreadyExistsException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PariDao {
    List<Pari> listUnPassedUserBets(String user_name) throws IOException;
    List<Pari> listAllParisByIdMatch(int idMatch) throws IOException;
    Pari addPari(Pari pari) throws PariAlreadyExistsException, IOException;
    void updateResultat(int idPari, boolean res) throws IOException;
    List<Pari> listParisPassedMatchs(String idUser) throws IOException;
    List<Pari> searchBet(String idUser, String filtreTeam, String filtreDate, int filtreResult) throws IOException;
    void deleteBet(String name, int idBet);
}
