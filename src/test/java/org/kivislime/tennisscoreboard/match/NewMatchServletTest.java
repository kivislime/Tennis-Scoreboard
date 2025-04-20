package org.kivislime.tennisscoreboard.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.service.LiveMatchService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NewMatchServletTest {

    private NewMatchServlet servlet;
    private LiveMatchService liveMatchService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new NewMatchServlet();

        liveMatchService = mock(LiveMatchService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);

        when(servletContext.getAttribute("liveMatchService")).thenReturn(liveMatchService);

        ServletConfig servletConfig = mock(ServletConfig.class);
        when(servletConfig.getServletContext()).thenReturn(servletContext);

        servlet.init(servletConfig);
    }

    @Test
    void doPost_shouldRedirectOnValidNames() throws Exception {
        when(request.getParameter("first_player_name")).thenReturn("Alice");
        when(request.getParameter("second_player_name")).thenReturn("Bob");

        UUID matchId = UUID.randomUUID();
        when(liveMatchService.createLiveMatchSession("Alice", "Bob")).thenReturn(matchId);

        when(request.getContextPath()).thenReturn("/");

        servlet.doPost(request, response);

        verify(response).sendRedirect("/match-score.jsp?uuid=" + matchId.toString());
    }

    @Test
    void doPost_shouldReturnBadRequestIfNamesAreInvalid() throws Exception {
        when(request.getParameter("first_player_name")).thenReturn("A");
        when(request.getParameter("second_player_name")).thenReturn("Bob");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonResponse = sw.toString();
        assertTrue(jsonResponse.contains("INVALID_PARAMETER"));
    }

    @Test
    void doPost_shouldReturnBadRequestIfNamesAreEqual() throws Exception {
        when(request.getParameter("first_player_name")).thenReturn("Alice");
        when(request.getParameter("second_player_name")).thenReturn("Alice");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonResponse = sw.toString();
        assertTrue(jsonResponse.contains("Players name must be different"));
    }
}
