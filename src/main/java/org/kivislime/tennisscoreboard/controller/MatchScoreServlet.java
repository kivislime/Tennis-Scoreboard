package org.kivislime.tennisscoreboard.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;
import org.kivislime.tennisscoreboard.service.MatchService;
import org.kivislime.tennisscoreboard.domain.PlayerNumber;
import org.kivislime.tennisscoreboard.util.JsonUtil;
import org.kivislime.tennisscoreboard.util.ServletUtil;
import org.kivislime.tennisscoreboard.util.ValidatorUtil;

import java.io.IOException;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        matchService = (MatchService) context.getAttribute("matchService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String matchId = req.getParameter("uuid");
        if (!ValidatorUtil.isValidParameter(matchId)) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Match id is empty or blank.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        }

        MatchScoreDto matchScoreDto = matchService.getLiveMatchScore(matchId);
        String matchScoreJson = JsonUtil.toJson(matchScoreDto);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(matchScoreJson);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String matchId = req.getParameter("uuid");
        String playerNumberStr = req.getParameter("player_number");

        if (!ValidatorUtil.isValidUuid(matchId) || !ValidatorUtil.isValidParameter(playerNumberStr)) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Match id or player number is invalid.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
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
                ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Invalid player number, must be 1 or 2.");
                ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
                return;
            }

            MatchScoreDto matchScoreDto = matchService.handleScoring(matchId, playerNumber);
            String matchScoreJson = JsonUtil.toJson(matchScoreDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(matchScoreJson);
        } catch (NumberFormatException e) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Invalid player number,  must be 1 or 2.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
        }

    }
}
