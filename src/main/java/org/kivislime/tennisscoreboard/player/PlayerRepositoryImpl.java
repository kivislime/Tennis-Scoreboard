package org.kivislime.tennisscoreboard.player;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.kivislime.tennisscoreboard.HibernateUtil;

import java.util.Optional;

public class PlayerRepositoryImpl implements PlayerRepository {
    //TODO: бросать исключение vs присылать optional?
    // Если выбрасываю всегда исключения на уровне сервиса без какой-либо предобработки, то смысл отправлять optional
    //TODO: remove getPlayer?
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
    public Optional<Player> addPlayer(String playerName) {
        //TODO: need to rework
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            session.beginTransaction();
//            Player player = Player.builder()
//                    .name(playerName)
//                    .build();
//            session.persist(player);
//            session.getTransaction().commit();
//            return Optional.ofNullable(player);
//        } catch (HibernateException e) {
//            throw new HibernateException(e);
//            return Optional.empty();
//        }
        return Optional.empty();
    }
}
