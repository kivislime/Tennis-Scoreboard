package org.kivislime.tennisscoreboard;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.kivislime.tennisscoreboard.repository.*;
import org.kivislime.tennisscoreboard.service.*;

@WebListener
public class ServiceContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        PlayerService playerService = new PlayerServiceImpl(playerRepository);

        MatchRepository matchRepository = new MatchRepositoryImpl();
        MatchQueryServiceImpl matchServiceImpl = new MatchQueryServiceImpl(matchRepository);

        LiveMatchRepository liveMatchRepository = new LiveMatchRepositoryImpl();
        FinishedMatchService finishedMatchService = new FinishedMatchServiceImpl(matchRepository);
        LiveMatchService liveMatchService = new LiveMatchServiceImpl(playerService, finishedMatchService, liveMatchRepository);

        ServletContext context = sce.getServletContext();
        context.setAttribute("matchService", matchServiceImpl);
        context.setAttribute("liveMatchService", liveMatchService);
    }
}
