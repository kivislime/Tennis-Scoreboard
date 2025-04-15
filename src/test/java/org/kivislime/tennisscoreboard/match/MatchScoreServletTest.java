package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.controller.MatchScoreServlet;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;
import org.kivislime.tennisscoreboard.domain.PlayerNumber;
import org.kivislime.tennisscoreboard.service.MatchService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MatchScoreServletTest {

    private final static String validUuid = "f99b97d4-74c3-4bc3-97f6-775fb6e1618c";
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
    private MatchScoreServlet servletUnderTest;
    private StringWriter responseWriter;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("matchService")).thenReturn(matchService);

        servletUnderTest = new MatchScoreServlet();
        servletUnderTest.init(servletConfig);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testDoGet_InvalidUuid() throws Exception {
        when(request.getParameter("uuid")).thenReturn("   ");

        servletUnderTest.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonError = responseWriter.toString();
        assertTrue(jsonError.contains("Match id is empty or blank."),
                "Expected error message for invalid match id");
    }

    @Test
    public void testDoGet_ValidUuid_ReturnsMatchScoreJson() throws Exception {
        when(request.getParameter("uuid")).thenReturn(validUuid);

        MatchScoreDto dummyDto = MatchScoreDto.builder().build();
        when(matchService.getLiveMatchScore(validUuid)).thenReturn(dummyDto);

        servletUnderTest.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = responseWriter.toString();
        assertTrue(jsonResponse.contains("matches") || jsonResponse.contains("match"),
                "Response should contain match data in JSON");
    }

    @Test
    public void testDoPost_InvalidParameters() throws Exception {
        when(request.getParameter("uuid")).thenReturn("");
        when(request.getParameter("player_number")).thenReturn("");

        servletUnderTest.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonError = responseWriter.toString();
        assertTrue(jsonError.contains("Match id or player number is invalid."),
                "Expected error message for invalid parameters");
    }

    @Test
    public void testDoPost_InvalidPlayerNumber() throws Exception {
        when(request.getParameter("uuid")).thenReturn(validUuid);
        when(request.getParameter("player_number")).thenReturn("3");

        servletUnderTest.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonError = responseWriter.toString();

        assertTrue(jsonError.contains("Invalid player number"),
                "Expected error message for invalid player number");
    }

    @Test
    public void testDoPost_ValidParameters_ReturnsMatchScoreJson() throws Exception {
        when(request.getParameter("uuid")).thenReturn(validUuid);
        when(request.getParameter("player_number")).thenReturn("1");

        MatchScoreDto dummyDto = MatchScoreDto.builder().build();
        when(matchService.handleScoring(validUuid, PlayerNumber.FIRST)).thenReturn(dummyDto);

        servletUnderTest.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = responseWriter.toString();
        assertTrue(jsonResponse.contains("match"), "Expected JSON response to contain match info");
    }
}
