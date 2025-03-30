package org.kivislime.tennisscoreboard;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private HibernateUtil() {
    }

    private static class SessionFactoryHolder {
        private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

        private static SessionFactory buildSessionFactory() { // Holder pattern
            //TODO: try catch?
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            return configuration.buildSessionFactory();
        }
    }

    public static SessionFactory getSessionFactory() {
        return SessionFactoryHolder.SESSION_FACTORY;
    }

}
