package dev.kayange.Multivendor.E.commerce.exception;

public class ActionNotPermittedException extends RuntimeException{
    private String message;

   public ActionNotPermittedException(String message){
        super(message);
    }
}
