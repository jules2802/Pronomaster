package exceptions;

public class PariAlreadyExistsException extends Exception {

    private  static final long serialVersionUID =1L;

    public PariAlreadyExistsException(int idPari){
        super("The bet with id="+ idPari + " already exists.");
    }

    public PariAlreadyExistsException(int idPari, Throwable t){
        super("The bet with id="+ idPari + " already exists.",t);
    }
}
