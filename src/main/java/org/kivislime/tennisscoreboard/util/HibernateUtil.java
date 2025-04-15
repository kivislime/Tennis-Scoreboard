package org.kivislime.tennisscoreboard.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private HibernateUtil() { }

    private static final SessionFactory SESSION_FACTORY;

    static {
        try {
            String configFile = System.getProperty("hibernate.config", "hibernate.cfg.xml");
            Configuration configuration = new Configuration();
            configuration.configure(configFile);
            SESSION_FACTORY = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
