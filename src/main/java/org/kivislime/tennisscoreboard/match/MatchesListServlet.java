package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.JsonUtil;
import org.kivislime.tennisscoreboard.ServletUtil;
import org.kivislime.tennisscoreboard.ValidatorUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/matches")
public class MatchesListServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        matchService = (MatchService) context.getAttribute("matchService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            totalPages = matchService.getTotalPagesByPlayerName(playerName);
            matchDtoList = matchService.getMatchesByPlayerName(playerName, pageNumber);
        } else if (ValidatorUtil.isValidParameter(playerName)) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "The length of the name must be more than 2, but less than 16 and consist only of Latin letters.");
            ServletUtil.sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        } else {
            totalPages = matchService.getTotalPages();
            matchDtoList = matchService.getMatches(pageNumber);
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