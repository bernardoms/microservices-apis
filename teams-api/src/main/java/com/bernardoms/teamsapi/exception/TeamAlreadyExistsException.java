package com.bernardoms.teamsapi.exception;

public class TeamAlreadyExistsException extends Exception {
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
