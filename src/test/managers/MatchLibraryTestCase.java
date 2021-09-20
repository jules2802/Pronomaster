package managers;

import dao.MatchDao;
import entities.Match;
import entities.Team;
import exceptions.MatchAlreadyExistsException;
import exceptions.MatchNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MatchLibraryTestCase {

    @InjectMocks
    private MatchLibrary matchLibrary =MatchLibrary.getInstance();

    @Mock
    private MatchDao matchDao;

    @Test
    public void shouldAddMatch() throws MatchAlreadyExistsException, IOException {
        //GIVEN
        Team teamIn=new Team(1,"PSG");
        Team teamOut=new Team(2,"OM");
        LocalDate date=LocalDate.now();
        Match match=new Match(17,teamIn,teamOut,date);

        //WHEN
        matchLibrary.addMatch(match);

        //THEN
        Mockito.verify(matchDao).addMatch(match);

    }

    @Test
    public void shouldListPassedMatchsWithoutScore() throws IOException {
        //GIVEN
        Team teamIn1=new Team(1,"PSG");
        Team teamOut1=new Team(2,"OM");
        LocalDate date1=LocalDate.now();
        Match match1=new Match(17,teamIn1,teamOut1,date1);

        Team teamIn2=new Team(1,"ALL");
        Team teamOut2=new Team(2,"POR");
        LocalDate date2=LocalDate.now();
        Match match2=new Match(18,teamIn2,teamOut2,date2);

        List<Match> matches=new ArrayList<>();
        matches.add(match1);
        matches.add(match2);

        Mockito.when(matchDao.listPassedMatchsWithoutScore()).thenReturn(matches);

        //WHEN
        List<Match> passedMatchsWithoutScore=matchLibrary.listPassedMatchsWithoutScore();

        //THEN
        assertThat(passedMatchsWithoutScore).isEqualTo(matches);
    }

    @Test
    public void shouldListPassedMatchsWithScore() throws IOException {
        //GIVEN
        Team teamIn1=new Team(1,"PSG");
        Team teamOut1=new Team(2,"OM");
        LocalDate date1=LocalDate.now();
        Match match1=new Match(17,teamIn1,teamOut1,date1);

        Team teamIn2=new Team(1,"ALL");
        Team teamOut2=new Team(2,"POR");
        LocalDate date2=LocalDate.now();
        Match match2=new Match(18,teamIn2,teamOut2,date2);

        List<Match> matches=new ArrayList<>();
        matches.add(match1);
        matches.add(match2);

        Mockito.when(matchDao.listPassedMatchsWithScore()).thenReturn(matches);

        //WHEN
        List<Match> passedMatchsWithScore=matchLibrary.listPassedMatchsWithScore();

        //THEN
        assertThat(passedMatchsWithScore).isEqualTo(matches);

    }

    @Test
    public void shouldListMatchUnpassedMatchs() throws IOException {
        //GIVEN
        Team teamIn1=new Team(1,"PSG");
        Team teamOut1=new Team(2,"OM");
        LocalDate date1=LocalDate.now().minusDays(2);
        Match match1=new Match(17,teamIn1,teamOut1,date1);

        Team teamIn2=new Team(1,"ALL");
        Team teamOut2=new Team(2,"POR");
        LocalDate date2=LocalDate.now().minusDays(2);
        Match match2=new Match(18,teamIn2,teamOut2,date2);

        List<Match> matches=new ArrayList<>();
        matches.add(match1);
        matches.add(match2);

        Mockito.when(matchDao.listUnpassedMatchs()).thenReturn(matches);

        //WHEN
        List<Match> unpassedMatchs=matchLibrary.listMatchUnpassedMatchs();

        //THEN
        assertThat(unpassedMatchs).isEqualTo(matches);

    }

    @Test
    public void shouldUpdateScore() throws IOException {
        //GIVEN
        int idMatch=2;
        int butIn=1;
        int butOut=3;

        //WHEN
        matchLibrary.updateScore(idMatch,butIn,butOut);

        //THEN
        Mockito.verify(matchDao).updateScore(idMatch,butIn,butOut);

    }

    @Test
    public void shouldGetMatchById() throws MatchNotFoundException, IOException {
        //GIVEN
        int idMatch=3;
        Team teamIn=new Team(1,"PSG");
        Team teamOut=new Team(2,"OM");
        LocalDate date=LocalDate.now();
        Match match=new Match(idMatch,teamIn,teamOut,date);
        Mockito.when(matchDao.getMatchById(idMatch)).thenReturn(match);

        //WHEN
        Match obtainedMatch=matchLibrary.getMatchById(idMatch);

        //THEN
        assertThat(obtainedMatch).isEqualTo(match);
    }

    @Test
    public void shouldListUnbetMatchs() throws IOException {
        //GIVEN
        String username="Lucie";

        Team teamIn1=new Team(1,"PSG");
        Team teamOut1=new Team(2,"OM");
        LocalDate date1=LocalDate.now();
        Match match1=new Match(17,teamIn1,teamOut1,date1);

        Team teamIn2=new Team(1,"ALL");
        Team teamOut2=new Team(2,"POR");
        LocalDate date2=LocalDate.now();
        Match match2=new Match(18,teamIn2,teamOut2,date2);

        List<Match> matches=new ArrayList<>();
        matches.add(match1);
        matches.add(match2);

        Mockito.when(matchDao.listUnbetMatchs(username)).thenReturn(matches);

        //WHEN
        List<Match> unbetMatches=matchLibrary.listUnbetMatchs(username);

        //THEN
        assertThat(unbetMatches).isEqualTo(matches);
    }

    @Test
    public void shouldLastThreeMatchesWithScore() throws IOException {
        //GIVEN
        Team teamIn1=new Team(1,"PSG");
        Team teamOut1=new Team(2,"OM");
        LocalDate date1=LocalDate.now();
        Match match1=new Match(17,teamIn1,teamOut1,date1);

        Team teamIn2=new Team(1,"ALL");
        Team teamOut2=new Team(2,"POR");
        LocalDate date2=LocalDate.now();
        Match match2=new Match(18,teamIn2,teamOut2,date2);

        Team teamIn3=new Team(1,"ALL");
        Team teamOut3=new Team(2,"ESP");
        LocalDate date3=LocalDate.now();
        Match match3=new Match(18,teamIn3,teamOut3,date3);

        List<Match> matches=new ArrayList<>();
        matches.add(match1);
        matches.add(match2);
        matches.add(match3);

        Mockito.when(matchDao.lastThreeMatchesWithScore()).thenReturn(matches);

        //WHEN
        List<Match> threeLastMatchesWithScore=matchLibrary.lastThreeMatchesWithScore();

        //THEN
        assertThat(threeLastMatchesWithScore).isEqualTo(matches);
    }
}
