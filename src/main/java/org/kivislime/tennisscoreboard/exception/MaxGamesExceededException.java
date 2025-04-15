package org.kivislime.tennisscoreboard.exception;

public class MaxGamesExceededException extends RuntimeException {
    public MaxGamesExceededException(String message) {
        super(message);
    }
}
