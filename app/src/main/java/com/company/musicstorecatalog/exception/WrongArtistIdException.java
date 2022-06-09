package com.company.musicstorecatalog.exception;

public class WrongArtistIdException extends RuntimeException{
    public WrongArtistIdException(String message) {super(message);}
    public WrongArtistIdException() {super();}
}
