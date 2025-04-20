package org.kivislime.tennisscoreboard.match;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.controller.MatchesListServlet;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.service.MatchQueryService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchesListServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private MatchQueryService matchQueryService;

    private MatchesListServlet servletUnderTest;
    private StringWriter responseWriter;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("matchQueryService")).thenReturn(matchQueryService);

        servletUnderTest = new MatchesListServlet();
        servletUnderTest.init(servletConfig);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testDoGet_InvalidPage() throws Exception {
        when(request.getParameter("page")).thenReturn("abc");
        when(request.getParameter("filter_by_player_name")).thenReturn("");

        servletUnderTest.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = responseWriter.toString();
        assertTrue(jsonResponse.contains("\"matches\""),
                "Expected JSON response to contain matches");
    }

    @Test
    public void testDoGet_InvalidPlayerName() throws Exception {
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("filter_by_player_name")).thenReturn("123");

        servletUnderTest.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonError = responseWriter.toString();
        assertTrue(jsonError.contains("The length of the name must be more than 2"),
                "Expected error message for invalid player name");
    }

    @Test
    public void testDoGet_EmptyMatches_DefaultQuery_ReturnsNotFound() throws Exception {
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("filter_by_player_name")).thenReturn("");

        when(matchQueryService.findMatches(1)).thenReturn(Collections.emptyList());
        when(matchQueryService.countPages()).thenReturn(0L);

        servletUnderTest.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGet_NonEmptyMatches_Returns200() throws Exception {
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("filter_by_player_name")).thenReturn("");

        MatchDto dummyMatch = MatchDto.builder().id(1L).build();
        List<MatchDto> dummyMatches = List.of(dummyMatch);

        when(matchQueryService.findMatches(1)).thenReturn(dummyMatches);
        when(matchQueryService.countPages()).thenReturn(1L);

        servletUnderTest.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = responseWriter.toString();
        assertTrue(jsonResponse.contains("\"matches\""),
                "Expected JSON response to contain matches");
    }
}
