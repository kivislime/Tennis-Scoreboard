package org.kivislime.tennisscoreboard;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.kivislime.tennisscoreboard.exception.MatchRepositoryException;
import org.kivislime.tennisscoreboard.exception.MatchScoreException;
import org.kivislime.tennisscoreboard.exception.MatchesNotFoundException;
import org.kivislime.tennisscoreboard.exception.MaxGamesExceededException;
import org.kivislime.tennisscoreboard.exception.PlayerRepositoryException;
import org.kivislime.tennisscoreboard.util.ServletUtil;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (MatchesNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "Matches not found");
            log.info("Match not found: {}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_NOT_FOUND, errorResponse);

        } catch (MatchScoreException e) {
            ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "Match score not found");
            log.info("Match score not found: {}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_NOT_FOUND, errorResponse);

        } catch (MaxGamesExceededException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "MAX_GAMES_EXCEEDED",
                    "The match has been terminated because the maximum number of games was reached"
            );
            log.info("Match forcibly terminated due to max games: {}", e.getMessage());
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_CONFLICT, errorResponse);

        } catch (MatchRepositoryException | PlayerRepositoryException e) {
            ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
            log.warn("Internal Server Error: {}", e.getMessage(), e);
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorResponse);

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
            log.error("Unknown Error: {}", e.getMessage(), e);
            ServletUtil.sendJsonError((HttpServletResponse) servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorResponse);
        }
    }
}
