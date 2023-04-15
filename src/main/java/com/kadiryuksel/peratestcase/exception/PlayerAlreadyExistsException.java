package com.kadiryuksel.peratestcase.exception;

public class PlayerAlreadyExistsException extends RuntimeException{
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
