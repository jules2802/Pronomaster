package managers;

import dao.PariDao;
import entities.Pari;
import exceptions.PariAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PariLibraryTestCase {
    @InjectMocks
    private PariLibrary pariLibrary = PariLibrary.getInstance();

    @Mock
    private PariDao pariDao;

    @Test
    public void shouldListUnPassedUserBets() throws IOException {
        //GIVEN
        String userName="Lucie";
        Pari bet1=new Pari(1,2,3,"Lucie");
        Pari bet2=new Pari(4,4,1,"Lucie");
        Pari bet3=new Pari(8,3,2,"Lucie");
        List<Pari> betsList=new ArrayList<>();
        betsList.add(bet1);
        betsList.add(bet2);
        betsList.add(bet3);
        Mockito.when(pariDao.listUnPassedUserBets(userName)).thenReturn(betsList);
        //WHEN
        List<Pari> betsFound=pariLibrary.listUnPassedUserBets(userName);

        //THEN
        assertThat(betsFound).isEqualTo(betsList);

    }

    @Test
    public void shouldAddPari() throws PariAlreadyExistsException, IOException {
        //GIVEN
        Pari pari=new Pari(2,5,3,"Lucie");

        //WHEN
        pariLibrary.addPari(pari);

        //THEN
        Mockito.verify(pariDao).addPari(pari);
    }

    @Test
    public void shouldListAllParisByIdMatch() throws IOException {
        //GIVEN
        int idMatch=2;
        Pari bet1=new Pari(2,5,3,"Lucie");
        Pari bet2=new Pari(2,1,3,"Thomas");
        Pari bet3=new Pari(2,2,1,"Pierre");
        List<Pari> betslist=new ArrayList<>();
        betslist.add(bet1);
        betslist.add(bet2);
        betslist.add(bet3);
        Mockito.when(pariDao.listAllParisByIdMatch(idMatch)).thenReturn(betslist);

        //WHEN
        List<Pari> betsFound= pariLibrary.listAllParisByIdMatch(idMatch);

        //THEN
        assertThat(betsFound).isEqualTo(betslist);
    }

    @Test
    public void shouldUpdateResultat() throws IOException {
        //GIVEN
        int idPari=2;
        boolean res=true;

        //WHEN
        pariLibrary.updateResultat(idPari,res);

        //THEN
        Mockito.verify(pariDao).updateResultat(idPari,res);

    }

    @Test
    public void shouldListParisPassedMatchs() throws IOException {
        //GIVEN
        String idUser="Lucie";
        Pari bet1=new Pari(1,2,3,"Lucie");
        Pari bet2=new Pari(4,4,1,"Lucie");
        Pari bet3=new Pari(8,3,2,"Lucie");
        List<Pari> betsList=new ArrayList<>();
        betsList.add(bet1);
        betsList.add(bet2);
        betsList.add(bet3);
        Mockito.when(pariDao.listParisPassedMatchs(idUser)).thenReturn(betsList);

        //WHEN
        List<Pari> betsFound= pariLibrary.listParisPassedMatchs(idUser);

        //THEN
        assertThat(betsFound).isEqualTo(betsList);
    }

    @Test
    public void shouldSearchBet() throws IOException{
        //GIVEN
        String idUser="pierre";
        String filtreTeam="PSG";
        String filtreDate="2018-11-24";
        int filtreResult=1;
        Pari bet1=new Pari(1,2,3,idUser);
        List<Pari> betsList=new ArrayList<>();
        betsList.add(bet1);
        Mockito.when(pariDao.searchBet(idUser, filtreTeam,filtreDate,filtreResult)).thenReturn(betsList);

        //WHEN
        List<Pari> betsFound= pariLibrary.searchBet(idUser, filtreTeam,filtreDate,filtreResult);

        //THEN
        assertThat(betsFound).isEqualTo(betsList);

    }

    @Test
    public void shouldDeleteBet(){
        //GIVEN
        String name="pierre";
        int idBet=12;

        //WHEN
        pariLibrary.deleteBet(name,idBet);

        //THEN
        Mockito.verify(pariDao).deleteBet(name,idBet);

    }

}
