package org.kivislime.tennisscoreboard.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.service.LiveMatchService;
import org.kivislime.tennisscoreboard.util.ServletUtil;
import org.kivislime.tennisscoreboard.util.ValidatorUtil;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private LiveMatchService liveMatchService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        liveMatchService = (LiveMatchService) context.getAttribute("liveMatchService");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

        UUID matchId = liveMatchService.createLiveMatchSession(firstPlayerName, secondPlayerName);
        String matchIdString = matchId.toString();

        resp.sendRedirect(req.getContextPath() + "match-score.jsp?uuid=" + matchIdString);
    }
}
