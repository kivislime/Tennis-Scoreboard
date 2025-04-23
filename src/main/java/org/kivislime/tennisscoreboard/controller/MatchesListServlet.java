package org.kivislime.tennisscoreboard.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.service.MatchQueryService;
import org.kivislime.tennisscoreboard.util.JsonUtil;
import org.kivislime.tennisscoreboard.util.ServletUtil;
import org.kivislime.tennisscoreboard.util.ValidatorUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/matches")
public class MatchesListServlet extends HttpServlet {
    private MatchQueryService matchQueryService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        matchQueryService = (MatchQueryService) context.getAttribute("matchQueryService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String page = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        int pageNumber = 1;
        try {
            if (ValidatorUtil.isValidPage(page)) {
                pageNumber = Integer.parseInt(page);
            }
        } catch (NumberFormatException e) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Invalid page number");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        }

        List<MatchDto> matchDtoList;
        long totalPages;

        if (ValidatorUtil.isValidName(playerName)) {
            totalPages = matchQueryService.countPagesByPlayer(playerName);
            matchDtoList = matchQueryService.findMatchesByPlayer(playerName, pageNumber);
        } else if (!ValidatorUtil.isValidParameter(playerName)) {
            totalPages = matchQueryService.countPages();
            matchDtoList = matchQueryService.findMatches(pageNumber);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "The length of the name must be more than 2, but less than 16 and consist only of Latin letters.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("matches", matchDtoList);
        responseData.put("totalPages", totalPages);
        responseData.put("currentPage", pageNumber);

        String jsonResponse = JsonUtil.toJson(responseData);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(jsonResponse);
    }
}