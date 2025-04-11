package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.JsonUtil;
import org.kivislime.tennisscoreboard.ValidatorUtil;

import java.io.IOException;
import java.util.List;

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
        //TODO: Context listener? Move service to context. Move mapper to context from service?
        //TODO: encoding?  req.setCharacterEncoding("UTF-8");

        String page = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        List<MatchDto> matchDtoList;
        if (ValidatorUtil.isValidParameter(page)) {
            //TODO: pagination?
        }

        if (ValidatorUtil.isValidName(playerName)) {
            matchDtoList = matchService.getMatchesByPlayerName(playerName);
        } else {
            matchDtoList = matchService.getMatches();
        }

        String allMatches = JsonUtil.toJson(matchDtoList);

        resp.getWriter().write(allMatches);
    }
}
