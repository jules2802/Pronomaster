package managers;

import dao.PariDao;
import dao.impl.PariDaoImpl;
import entities.Pari;
import exceptions.PariAlreadyExistsException;

import java.io.IOException;
import java.util.List;

public class PariLibrary {

    //Création d'un singleton:toujours la meme instance
    private static class ParieLibraryHolder {
        private final static PariLibrary instance = new PariLibrary();
    }

    private PariDao pariDao = new PariDaoImpl();


    public static PariLibrary getInstance() {

        return PariLibrary.ParieLibraryHolder.instance;
    }

    private PariLibrary() { }

    public List<Pari> listUnPassedUserBets(String user_name) throws IOException {

        return pariDao.listUnPassedUserBets(user_name);
    }

    public void addPari(Pari pari) throws PariAlreadyExistsException, IOException {

        pariDao.addPari(pari);
    }

    public List<Pari> listAllParisByIdMatch(int idMatch) throws IOException {

        return pariDao.listAllParisByIdMatch(idMatch);
    }
    public void updateResultat(int idPari, boolean res) throws IOException {
        pariDao.updateResultat(idPari,res);
    }

    public List<Pari> listParisPassedMatchs(String idUser) throws IOException {
        return pariDao.listParisPassedMatchs(idUser);
    }

    public List<Pari> searchBet(String idUser, String filtreTeam,String filtreDate,int filtreResult) throws IOException {return pariDao.searchBet(idUser, filtreTeam,filtreDate,filtreResult);}

    public void deleteBet(String name, int idBet){pariDao.deleteBet(name, idBet);}
}
