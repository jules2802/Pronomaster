package entities;

public class Pari {
    private int result;
    private Integer idPari;
    private Match match;
    private int butIn;
    private int butOut;
    private String idUser;
    private int idMatch;


    public Pari(Integer idPari, Match match, int butIn, int butOut, String idUser) {
        this.idPari=idPari;
        this.match = match;
        this.butIn=butIn;
        this.butOut=butOut;
        this.idUser = idUser;
    }

    public Pari(int idPari, Match match, int butIn, int butOut, String idUser, int result) {
        this.idPari=idPari;
        this.match = match;
        this.butIn=butIn;
        this.butOut=butOut;
        this.idUser = idUser;
        this.result=result;
    }



    public Pari( int idMatch, int butIn, int butOut, String user_name) {

        this.idMatch=idMatch;
        this.butIn=butIn;
        this.butOut=butOut;
        this.idUser =user_name;
    }

    public int getIdPari() {
        return idPari;
    }

    public void setIdPari(int idPari) {
        this.idPari = idPari;
    }

    public Match getMatch() {

        return this.match;
    }

    public String getIdUser() {

        return this.idUser;
    }

    public int getButIn() {

        return this.butIn;
    }

    public void setButIn(int butIn) {

        this.butIn = butIn;
    }

    public int getButOut() {

        return this.butOut;
    }

    public void setButOut(int butOut) {

        this.butOut = butOut;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }
    public int getIdMatch() {
        return idMatch;
    }
}
