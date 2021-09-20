package exceptions;

public class UserAlreadyExistsException extends Exception {

    private  static final long serialVersionUID =1L;

    public UserAlreadyExistsException(String username){
        super("The user with name="+ username + " already exists.");
    }

    public UserAlreadyExistsException(String username, Throwable t){
        super("The user with name="+ username + " already exists.",t);
    }
}
