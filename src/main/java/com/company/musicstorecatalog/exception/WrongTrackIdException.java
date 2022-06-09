package com.company.musicstorecatalog.exception;

public class WrongTrackIdException extends RuntimeException{
    public WrongTrackIdException(String message) {super(message);}
    public WrongTrackIdException() {super();}
}
