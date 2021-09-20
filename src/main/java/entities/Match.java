package entities;

import java.time.LocalDate;

public class Match {

    private int idMatch;
    private Team teamIn;
    private Team teamOut;
    private int butIn;
    private int butOut;
    private LocalDate date;


    public Match(int idMatch, Team teamIn, Team teamOut, int butIn, int butOut, LocalDate date){
        this.idMatch=idMatch;
        this.teamIn = teamIn;
        this.teamOut = teamOut;
        this.butIn=butIn;
        this.butOut=butOut;
        this.date=date;
    }

    public Match(int idMatch, Team teamIn, Team teamOut, LocalDate date){
        this.idMatch=idMatch;
        this.teamIn = teamIn;
        this.teamOut = teamOut;
        this.date=date;
    }

    public Match(Team teamIn, Team teamOut, LocalDate date){
        this.teamIn=teamIn;
        this.teamOut=teamOut;
        this.date=date;
    }



    public int getIdMatch(){ return this.idMatch;}
    public void setIdMatch(int idmatch){this.idMatch=idmatch;}
    public Team getTeamIn(){return this.teamIn;}
    public Team getTeamOut(){return this.teamOut;}
    public int getButIn(){return this.butIn;}
    public int getButOut(){return this.butOut;}
    public void setButIn(int but){this.butIn=but;}
    public void setButOut(int but){this.butOut=but;}
    public LocalDate getDate(){return this.date; }


}
