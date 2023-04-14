package com.kadiryuksel.peratestcase.exception;

public class PlayerAlreadyExistsException extends RuntimeException{
    public PlayerAlreadyExistsException(String playerName, String teamName) {
        super(String.format("%s already exists in %s.", playerName, teamName));
    }
}
