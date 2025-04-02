package org.kivislime.tennisscoreboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class MatchesList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        //TODO: Context listener? Move service. Move mapper
        MatchService matchesService = new MatchService(new MatchRepositoryImpl());

        List<MatchDto> matchDtoList = matchesService.getMatches();
        String allMatches = JsonUtil.toJson(matchDtoList);

        resp.getWriter().write(allMatches);
    }
}
