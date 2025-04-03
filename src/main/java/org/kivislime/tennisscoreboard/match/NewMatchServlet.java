package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class NewMatchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String firstPlayerName = req.getParameter("first_player_name");
        String secondPlayerName = req.getParameter("second_player_name");

        //TODO: validate in util class
        if (firstPlayerName == null || secondPlayerName == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


    }
}
