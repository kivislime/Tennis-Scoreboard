package org.kivislime.tennisscoreboard.exception;

import org.hibernate.HibernateException;

public class MatchRepositoryException extends RuntimeException {
    public MatchRepositoryException(String message, HibernateException e) {
        super(message, e);
    }
}
