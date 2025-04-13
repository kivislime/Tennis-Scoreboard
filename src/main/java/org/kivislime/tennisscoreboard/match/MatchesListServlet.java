package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;
import org.kivislime.tennisscoreboard.JsonUtil;
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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(JsonUtil.toJson(errorResponse));
            return;
        }

        List<MatchDto> matchDtoList;
        long totalMatches;

        if (ValidatorUtil.isValidName(playerName)) {
            matchDtoList = matchService.getMatchesByPlayerName(playerName, pageNumber);
            totalMatches = matchService.getTotalMatchesByPlayerName(playerName);
            if (totalMatches < pageNumber) {
                ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Number of page greater than the number of total pages: " + totalMatches);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(JsonUtil.toJson(errorResponse));
                return;
            }
        } else if (ValidatorUtil.isValidParameter(playerName)) {
            ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "The name must consist only of Latin letters.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(JsonUtil.toJson(errorResponse));
            return;
        } else {
            matchDtoList = matchService.getMatches(pageNumber);
            totalMatches = matchService.getTotalMatches();
            if (totalMatches < pageNumber) {
                ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", "Number of page greater than the number of total pages: " + totalMatches);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(JsonUtil.toJson(errorResponse));
                return;
            }
        }

        int totalPages = (int) Math.ceil((double) totalMatches / PaginationConfig.PAGE_SIZE);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("matches", matchDtoList);
        responseData.put("totalPages", totalPages);
        responseData.put("currentPage", pageNumber);

        String jsonResponse = JsonUtil.toJson(responseData);
        //TODO: дорабоать matches, match-score чтобы красиво отображало ошибку в закругленной плашке как в index.jsp
        // переписываю ли я комментарии ошибок? смысл этого если так?
        resp.getWriter().write(jsonResponse);
    }
}