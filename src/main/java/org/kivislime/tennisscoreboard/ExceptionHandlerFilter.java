package org.kivislime.tennisscoreboard;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.match.MatchNotFoundException;
import org.kivislime.tennisscoreboard.match.MatchRepositoryException;
import org.kivislime.tennisscoreboard.match.MatchScoreException;
import org.kivislime.tennisscoreboard.match.MaxGamesExceededException;
import org.kivislime.tennisscoreboard.player.PlayerRepositoryException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandlerFilter implements Filter {
    private final Logger logger = Logger.getLogger(ExceptionHandlerFilter.class.getName());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (MatchNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "Match does not exist");
            logger.log(Level.INFO, "Match not found: {0}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_NOT_FOUND, errorResponse);

        } catch (MatchScoreException e) {
            ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "Match score not found");
            logger.log(Level.INFO, "Match score not found: {0}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_NOT_FOUND, errorResponse);

        } catch (MatchRepositoryException | PlayerRepositoryException e) {
            ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
            logger.log(Level.WARNING, "Internal Server Error: {0}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorResponse);

        } catch (MaxGamesExceededException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "MAX_GAMES_EXCEEDED",
                    "The match has been terminated because the maximum number of games was reached"
            );
            logger.log(Level.INFO, "Match forcibly terminated due to max games: {0}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_CONFLICT, errorResponse);

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
            logger.log(Level.WARNING, "Unknown Error: {0}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorResponse);
        }
    }
}
