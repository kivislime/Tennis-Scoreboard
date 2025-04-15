package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.controller.NewMatchServlet;
import org.kivislime.tennisscoreboard.service.MatchService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewMatchServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private MatchService matchService;

    private NewMatchServlet servletUnderTest;
    private StringWriter responseWriter;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("matchService")).thenReturn(matchService);

        servletUnderTest = new NewMatchServlet();
        servletUnderTest.init(servletConfig);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testDoPost_InvalidNames_ReturnsBadRequest() throws Exception {
        when(request.getParameter("first_player_name")).thenReturn("a");
        when(request.getParameter("second_player_name")).thenReturn("Bob");

        servletUnderTest.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonError = responseWriter.toString();
        assertTrue(jsonError.contains("The length of the name must be more than 2"),
                "Expected error message for invalid name");
    }

    @Test
    public void testDoPost_SameNames_ReturnsBadRequest() throws Exception {
        when(request.getParameter("first_player_name")).thenReturn("Alice");
        when(request.getParameter("second_player_name")).thenReturn("Alice");

        servletUnderTest.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonError = responseWriter.toString();
        assertTrue(jsonError.contains("Players name must be different."),
                "Expected error message for same names");
    }

    @Test
    public void testDoPost_ValidNames_RedirectsToMatchScore() throws Exception {
        when(request.getParameter("first_player_name")).thenReturn("Alice");
        when(request.getParameter("second_player_name")).thenReturn("Bob");

        UUID dummyUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(matchService.createLiveMatchSession("Alice", "Bob")).thenReturn(dummyUuid);

        when(request.getContextPath()).thenReturn("/TennisScoreboard");

        servletUnderTest.doPost(request, response);

        verify(response).sendRedirect("/TennisScoreboard/match-score.jsp?uuid=" + dummyUuid.toString());
    }
}
