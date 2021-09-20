package exceptions;

public class TeamNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public TeamNotFoundException(int teamId) {
        super( teamId+ " not found");
    }

    public TeamNotFoundException(int teamId, Throwable t) {
        super( teamId+ " not found",t);
    }
}
