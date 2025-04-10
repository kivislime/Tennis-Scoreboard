package org.kivislime.tennisscoreboard.player;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.kivislime.tennisscoreboard.HibernateUtil;

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
            throw new HibernateException(e); //TODO: изменить на собственный тип
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
