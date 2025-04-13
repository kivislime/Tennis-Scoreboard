package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.ServletUtil;
import org.kivislime.tennisscoreboard.ValidatorUtil;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        matchService = (MatchService) context.getAttribute("matchService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstPlayerName = req.getParameter("first_player_name");
        String secondPlayerName = req.getParameter("second_player_name");

        if (!ValidatorUtil.isValidName(firstPlayerName) || !ValidatorUtil.isValidName(secondPlayerName)) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "The length of the name must be more than 2, but less than 16 and consist only of Latin letters.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        }

        if (firstPlayerName.equals(secondPlayerName)) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Players name must be different.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        }

        UUID matchId = matchService.createLiveMatchSession(firstPlayerName, secondPlayerName);
        String matchIdString = matchId.toString();

        resp.sendRedirect(req.getContextPath() + "/match-score.jsp?uuid=" + matchIdString);

        //TODO: remove, or add logger
        System.out.println("New match UUID: " + matchIdString);
    }
}
