package org.kivislime.tennisscoreboard.match;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.kivislime.tennisscoreboard.HibernateUtil;

import java.util.List;

public class MatchRepositoryImpl implements MatchRepository {
    public List<Match> getMatches(Integer pageNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int offset = (pageNumber - 1) * PaginationConfig.PAGE_SIZE;

            String hql = "from Match";
            Query<Match> query = session.createQuery(hql, Match.class)
                    .setFirstResult(offset)
                    .setMaxResults(PaginationConfig.PAGE_SIZE);

            return query.getResultList();
        } catch (HibernateException e) {
            throw new MatchRepositoryException("Error when receiving matches: ", e);
        }
    }

    @Override
    public long getTotalMatches() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(m) from Match m";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new MatchRepositoryException("Error when receiving matches: ", e);
        }
    }

    public List<Match> getMatchesByPlayerName(String playerName, Integer pageNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int offset = (pageNumber - 1) * PaginationConfig.PAGE_SIZE;

            String hql = "from Match where firstPlayer.name = :playerName or secondPlayer.name = :playerName";

            List<Match> matches = session.createQuery(hql, Match.class)
                    .setParameter("playerName", playerName)
                    .setFirstResult(offset)
                    .setMaxResults(PaginationConfig.PAGE_SIZE)
                    .getResultList();

            if (matches.isEmpty()) {
                throw new MatchesNotFoundException("Matches not found in match repository for player: " + playerName +
                        ", and page number: " + pageNumber);
            }

            return matches;
        } catch (HibernateException e) {
            throw new MatchRepositoryException("Error when receiving matches by player name: " + playerName, e);
        }
    }

    @Override
    public long getTotalMatchesByPlayerName(String playerName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(m) from Match m where m.firstPlayer.name = :playerName or m.secondPlayer.name = :playerName";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("playerName", playerName);

            Long count = query.uniqueResult();
            return count != null ? count : 0L;
        } catch (HibernateException e) {
            throw new MatchRepositoryException("Error when retrieving total matches for player: " + playerName, e);
        }
    }

    public Match addMatch(Match match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(match);
            session.getTransaction().commit();
            return match;
        } catch (HibernateException e) {
            throw new MatchRepositoryException("Error when adding match: " + match, e);
        }
    }
}
