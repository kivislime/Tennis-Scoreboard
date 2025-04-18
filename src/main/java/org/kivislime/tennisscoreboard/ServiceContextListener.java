package org.kivislime.tennisscoreboard;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.kivislime.tennisscoreboard.repository.MatchRepository;
import org.kivislime.tennisscoreboard.repository.MatchRepositoryImpl;
import org.kivislime.tennisscoreboard.service.MatchServiceImpl;
import org.kivislime.tennisscoreboard.repository.PlayerRepository;
import org.kivislime.tennisscoreboard.repository.PlayerRepositoryImpl;
import org.kivislime.tennisscoreboard.service.PlayerService;
import org.kivislime.tennisscoreboard.service.PlayerServiceImpl;

@WebListener
public class ServiceContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        PlayerService playerService = new PlayerServiceImpl(playerRepository);

        MatchRepository matchRepository = new MatchRepositoryImpl();
        MatchServiceImpl matchServiceImpl = new MatchServiceImpl(matchRepository, playerService);

        ServletContext context = sce.getServletContext();
        context.setAttribute("matchService", matchServiceImpl);
    }
}
