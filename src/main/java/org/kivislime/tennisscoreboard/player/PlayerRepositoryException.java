package org.kivislime.tennisscoreboard.player;

public class PlayerRepositoryException extends RuntimeException {
    public PlayerRepositoryException(String message) {
        super(message);
    }

    public PlayerRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
