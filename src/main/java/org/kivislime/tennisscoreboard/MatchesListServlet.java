package org.kivislime.tennisscoreboard;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/matches")
public class MatchesListServlet extends HttpServlet {
    MatchService matchService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        matchService = (MatchService) context.getAttribute("matchService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        //TODO: Context listener? Move service to context. Move mapper to context from service?
        //TODO: encoding?  req.setCharacterEncoding("UTF-8");

        String page = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        List<MatchDto> matchDtoList = Collections.emptyList();
        //TODO: validator?
        if (page != null) {
            //TODO: pagination?
        }

        if (playerName != null && !playerName.isEmpty()) {
            matchDtoList = matchService.getMatchesByPlayerName(playerName);
        } else {
            matchDtoList = matchService.getMatches();
        }

        String allMatches = JsonUtil.toJson(matchDtoList);

        resp.getWriter().write(allMatches);
    }
}
