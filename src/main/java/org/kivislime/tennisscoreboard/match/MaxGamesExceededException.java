package org.kivislime.tennisscoreboard.match;

public class MaxGamesExceededException extends RuntimeException {
    public MaxGamesExceededException(String message) {
        super(message);
    }
}
