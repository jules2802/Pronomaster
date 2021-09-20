package exceptions;

public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String username) {
        super( username+ " not found");
    }

    public UserNotFoundException(String username, Throwable t) {
        super( username+ " not found",t);
    }
}
