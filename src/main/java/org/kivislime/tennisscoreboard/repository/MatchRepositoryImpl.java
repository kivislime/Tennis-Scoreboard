package org.kivislime.tennisscoreboard.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.kivislime.tennisscoreboard.config.PaginationConfig;
import org.kivislime.tennisscoreboard.domain.Match;
import org.kivislime.tennisscoreboard.exception.MatchRepositoryException;
import org.kivislime.tennisscoreboard.exception.MatchesNotFoundException;
import org.kivislime.tennisscoreboard.util.HibernateUtil;

import java.util.List;

public class MatchRepositoryImpl implements MatchRepository {
    public List<Match> findAll(Integer pageNumber) {
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
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(m) from Match m";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new MatchRepositoryException("Error when receiving matches: ", e);
        }
    }

    public List<Match> findByPlayerName(String playerName, Integer pageNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int offset = (pageNumber - 1) * PaginationConfig.PAGE_SIZE;

            String hql = "from Match where firstPlayer.name = :playerName or secondPlayer.name = :playerName";

            List<Match> matches = session.createQuery(hql, Match.class)
                    .setParameter("playerName", playerName)
                    .setFirstResult(offset)
                    .setMaxResults(PaginationConfig.PAGE_SIZE)
                    .getResultList();

            if (matches.isEmpty()) {
                throw new MatchesNotFoundException(String.format("Matches not found in match repository for player: %s, and page number: %s",
                        playerName, pageNumber));
            }

            return matches;
        } catch (HibernateException e) {
            throw new MatchRepositoryException(String.format("Error when receiving matches by player name: %s", playerName), e);
        }
    }

    @Override
    public long countByPlayerName(String playerName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(m) from Match m where m.firstPlayer.name = :playerName or m.secondPlayer.name = :playerName";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("playerName", playerName);

            Long count = query.uniqueResult();
            return count != null ? count : 0L;
        } catch (HibernateException e) {
            throw new MatchRepositoryException(String.format("Error when retrieving total matches for player: %s", playerName), e);
        }
    }

    public Match persist(Match match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(match);
            session.getTransaction().commit();
            return match;
        } catch (HibernateException e) {
            throw new MatchRepositoryException(String.format("Error when adding match: %s", match), e);
        }
    }
}
