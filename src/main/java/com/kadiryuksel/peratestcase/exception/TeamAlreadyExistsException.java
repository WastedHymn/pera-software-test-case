package com.kadiryuksel.peratestcase.exception;

public class TeamAlreadyExistsException extends RuntimeException {

    public TeamAlreadyExistsException(String teamName) {
        super(String.format("%s already exists.", teamName));
    }
}
