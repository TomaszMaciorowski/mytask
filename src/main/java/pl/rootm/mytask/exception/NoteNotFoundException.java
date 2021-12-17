package pl.rootm.mytask.exception;

public class NoteNotFoundException extends  RuntimeException {

    public NoteNotFoundException(String message){
        super(message);
    }

}
