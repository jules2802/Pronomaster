package entities;

public class Administrator extends User {


    public Administrator(String name, String mdp, String mail) {
        super(name, mdp, mail, 1);
    }
}


