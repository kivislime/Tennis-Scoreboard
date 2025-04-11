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
        if (!ValidatorUtil.isValidParameter(matchId)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Match id is empty or blank.");
            resp.getWriter().write(JsonUtil.toJson(errorResponse));
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

        if (!ValidatorUtil.isValidUuid(matchId) || !ValidatorUtil.isValidParameter(playerNumberStr)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Match id or player number is invalid.");
            resp.getWriter().write(JsonUtil.toJson(errorResponse));
            return;
        }

        try {
            Integer playerNumberInt = Integer.parseInt(playerNumberStr);
            PlayerNumber playerNumber;
            if (playerNumberInt.equals(1)) {
                playerNumber = PlayerNumber.FIRST;
            } else if (playerNumberInt.equals(2)) {
                playerNumber = PlayerNumber.SECOND;
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Invalid player number, must be 1 or 2.");
                resp.getWriter().write(JsonUtil.toJson(errorResponse));
                return;
            }

            MatchScoreDto matchScoreDto = matchService.handleScoring(matchId, playerNumber);
            String matchScoreJson = JsonUtil.toJson(matchScoreDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(matchScoreJson);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Invalid player number,  must be 1 or 2.");
            resp.getWriter().write(JsonUtil.toJson(errorResponse));
        }

    }
}
