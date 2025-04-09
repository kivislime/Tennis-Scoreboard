package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        matchService = (MatchService) context.getAttribute("matchService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstPlayerName = req.getParameter("first_player_name");
        String secondPlayerName = req.getParameter("second_player_name");

        //TODO: validate in util class
        if (firstPlayerName == null || secondPlayerName == null ||
                firstPlayerName.isBlank() || secondPlayerName.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (firstPlayerName.equals(secondPlayerName)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //TODO: Set status code in other controllers, when success and fail in error response
        UUID matchId = matchService.createLiveMatchSession(firstPlayerName, secondPlayerName);
        String matchIdString = matchId.toString();

        resp.sendRedirect(req.getContextPath() + "/match-score.jsp?uuid=" + matchIdString);

        //TODO: remove, or add logger
        System.out.println("New match UUID: " + matchIdString);
    }
}
