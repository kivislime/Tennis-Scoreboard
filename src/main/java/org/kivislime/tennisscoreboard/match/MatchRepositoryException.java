package org.kivislime.tennisscoreboard.match;

import org.hibernate.HibernateException;

public class MatchRepositoryException extends RuntimeException {

    public MatchRepositoryException(String message) {
        super(message);
    }

    public MatchRepositoryException(String message, HibernateException e) {
        super(message, e);
    }
}
