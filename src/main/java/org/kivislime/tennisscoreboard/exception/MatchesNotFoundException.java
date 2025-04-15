package org.kivislime.tennisscoreboard.exception;

public class MatchesNotFoundException extends RuntimeException {
    public MatchesNotFoundException(String message) {
        super(message);
    }
}
