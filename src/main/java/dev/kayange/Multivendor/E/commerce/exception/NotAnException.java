package dev.kayange.Multivendor.E.commerce.exception;

public class NotAnException extends RuntimeException{
    private String message;
    public NotAnException(String message){super(message);}
}
