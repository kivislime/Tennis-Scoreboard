package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.JsonUtil;

import java.io.IOException;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        matchService = (MatchService) context.getAttribute("matchService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String matchId = req.getParameter("uuid");
        if (matchId == null || matchId.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MatchScoreDto matchScoreDto = matchService.getLiveMatchScore(matchId);
        String matchScoreJson = JsonUtil.toJson(matchScoreDto);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(matchScoreJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String matchId = req.getParameter("uuid");
        String playerNumberStr = req.getParameter("player_number");

        if (matchId == null || playerNumberStr == null || matchId.isBlank() || playerNumberStr.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //TODO: if match end? may be returning in dto field "hasWinner"? Dont check winner field in controller? ask gpt
        try {
            Integer playerNumberInt = Integer.parseInt(playerNumberStr);
            PlayerNumber playerNumber;
            if (playerNumberInt.equals(1)) {
                playerNumber = PlayerNumber.FIRST;
            } else if (playerNumberInt.equals(2)) {
                playerNumber = PlayerNumber.SECOND;
            } else {
                throw new RuntimeException("Invalid player number");
            }

            MatchScoreDto matchScoreDto = matchService.handleScoring(matchId, playerNumber);
            String matchScoreJson = JsonUtil.toJson(matchScoreDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(matchScoreJson);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new RuntimeException(e);
        }

    }
}
