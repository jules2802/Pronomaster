package exceptions;

public class MatchNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public MatchNotFoundException(int idMatch) {
        super( idMatch+ " not found");
    }

    public MatchNotFoundException(int idMatch, Throwable t) {
        super( idMatch+ " not found",t);
    }
}
