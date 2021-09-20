package entities;

public class Player extends User {

    private int score;

    public Player(String name, String mdp, String mail, int score){
        super(name,mdp,mail,0);
        this.score=score;
    }

    public Player(String name, String mdp, String mail){
        super(name,mdp,mail,0);
        this.score=0;
    }
    //Création d'un deuxième constructeur permettant de créer un joueur à partir des éléments de la base de données sans le mot de passe (sécurité)
    public Player(String name, int score){
        super(name);
        this.score=score;
    }

    public Player(int admin, String name){
        super(name);
        this.admin=admin;
    }
    public int getScore(){
        return this.score;
    }

    public void augmenterScore(){
        score++;
    }

    public void reduireScore(){score--;}
}
