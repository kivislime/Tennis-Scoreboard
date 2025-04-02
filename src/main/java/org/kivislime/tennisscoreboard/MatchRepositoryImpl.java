package org.kivislime.tennisscoreboard;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MatchRepositoryImpl {

    public List<Match> getMatches() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "from Match";
            Query<Match> query = session.createQuery(hql, Match.class);
            return query.getResultList();
        }
    }
}
