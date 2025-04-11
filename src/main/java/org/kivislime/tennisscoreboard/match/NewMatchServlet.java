package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.JsonUtil;
import org.kivislime.tennisscoreboard.ValidatorUtil;

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

        if (!ValidatorUtil.isValidName(firstPlayerName) || !ValidatorUtil.isValidName(secondPlayerName)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "The name must consist only of Latin characters.");
            resp.getWriter().write(JsonUtil.toJson(errorResponse));
            return;
        }

        if (firstPlayerName.equals(secondPlayerName)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Players name must be different.");
            resp.getWriter().write(JsonUtil.toJson(errorResponse));
            return;
        }

        UUID matchId = matchService.createLiveMatchSession(firstPlayerName, secondPlayerName);
        String matchIdString = matchId.toString();

        resp.sendRedirect(req.getContextPath() + "/match-score.jsp?uuid=" + matchIdString);

        //TODO: remove, or add logger
        System.out.println("New match UUID: " + matchIdString);
    }
}
