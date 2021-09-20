package entities;

public class User {

    protected String name;
    protected String password;
    protected String mail;
    protected int admin;

    public User(String name, String password, String mail, int admin){
        this.name = name;
        this.password = password;
        this.mail=mail;
        this.admin=admin;
    }
    public User(String name){
        this.name = name;
    }

    public User(String name, int admin){
        this.name = name;
        this.admin=admin;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password =password;
    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        this.mail=mail;
    }

    public int getAdmin(){
        return admin;
    }

    public void setAdmin(int admin){
        this.admin=admin;
    }

}