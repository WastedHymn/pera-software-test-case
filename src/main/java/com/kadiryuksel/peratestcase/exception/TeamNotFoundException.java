package com.kadiryuksel.peratestcase.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(long teamId) {
        super(String.format("Team ID: %d does not exists.", teamId));
    }

    public TeamNotFoundException(String teamName) {
        super(String.format("%s does not exists.", teamName));
    }
}
