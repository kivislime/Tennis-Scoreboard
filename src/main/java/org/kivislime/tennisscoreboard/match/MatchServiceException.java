package org.kivislime.tennisscoreboard.match;

public class MatchServiceException extends RuntimeException {
    public MatchServiceException(String message) {
        super(message);
    }

    public MatchServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
