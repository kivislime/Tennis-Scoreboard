package org.kivislime.tennisscoreboard;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MatchRepositoryImpl implements MatchRepository {

    public List<Match> getMatches() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //TODO: add beginTransaction where need to dml operation
            // session.beginTransaction();
            String hql = "from Match";
            Query<Match> query = session.createQuery(hql, Match.class);
            return query.getResultList();
        }
    }

    public List<Match> getMatchesByPlayerName(String playerName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Match where firstPlayer.name = :playerName or secondPlayer.name = :playerName";

            List<Match> matches = session.createQuery(hql, Match.class)
                    .setParameter("playerName", playerName)
                    .getResultList();

            if (matches.isEmpty()) {
                throw new HibernateException("Match not found");
            }

            return matches;
        }
    }
}
