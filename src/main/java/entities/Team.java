package entities;

public class Team {

    private String name;
    private int idEquipe;

    public Team(int idEquipe, String name){
        this.idEquipe=idEquipe;
        this.name=name;
    }

    public int getIdEquipe(){return this.idEquipe;}
    public String getName(){return this.name;}

}
