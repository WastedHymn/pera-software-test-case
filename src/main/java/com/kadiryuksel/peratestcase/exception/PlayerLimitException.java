package com.kadiryuksel.peratestcase.exception;

public class PlayerLimitException extends RuntimeException {
    public PlayerLimitException(String message) {
        super(message);
    }
}
