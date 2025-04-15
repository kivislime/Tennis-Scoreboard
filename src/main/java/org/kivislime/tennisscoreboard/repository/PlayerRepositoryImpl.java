package org.kivislime.tennisscoreboard.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.kivislime.tennisscoreboard.exception.PlayerRepositoryException;
import org.kivislime.tennisscoreboard.domain.Player;
import org.kivislime.tennisscoreboard.util.HibernateUtil;

import java.util.Optional;

public class PlayerRepositoryImpl implements PlayerRepository {
    public Optional<Player> getPlayer(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Player where name = :name";
            Player player = session.createQuery(hql, Player.class)
                    .setParameter("name", name)
                    .uniqueResult();

            return Optional.ofNullable(player);
        } catch (HibernateException e) {
            throw new PlayerRepositoryException("Error when receiving player by name: " + name + e.getMessage());
        }
    }

    @Override
    public Player addPlayer(Player player) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(player);
            session.getTransaction().commit();
            return player;
        } catch (HibernateException e) {
            throw new PlayerRepositoryException("Error when adding player by name: " + player.getName() + e.getMessage());
        }
    }
}
