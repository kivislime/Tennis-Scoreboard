package org.kivislime.tennisscoreboard;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServiceContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        MatchRepository matchRepository = new MatchRepositoryImpl();
        MatchServiceImpl matchServiceImpl = new MatchServiceImpl(matchRepository);
        ServletContext context = sce.getServletContext();
        context.setAttribute("matchService", matchServiceImpl);
    }
}
