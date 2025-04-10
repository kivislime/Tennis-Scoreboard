package org.kivislime.tennisscoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.match.*;
import org.kivislime.tennisscoreboard.player.Player;
import org.kivislime.tennisscoreboard.player.PlayerService;
import org.kivislime.tennisscoreboard.player.PlayerServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchServiceImplTest {
    private MatchRepository mockMatchRepository;
    private PlayerService mockPlayerService;
    private MatchServiceImpl matchServiceImpl;

    @BeforeEach
    void setUp() {
        mockMatchRepository = mock(MatchRepositoryImpl.class);
        mockPlayerService = mock(PlayerServiceImpl.class);
        matchServiceImpl = new MatchServiceImpl(mockMatchRepository, mockPlayerService);
    }

    @Test
    void testGetMatches() {
        Player player1 = new Player(1L, "Johan");
        Player player2 = new Player(2L, "Pohan");
        Player player3 = new Player(3L, "Kohan");

        Match match1 = new Match(1L, player1, player2, player1);
        Match match2 = new Match(2L, player1, player3, player3);

        when(mockMatchRepository.getMatches()).thenReturn(Arrays.asList(match1, match2));

        List<MatchDto> dtos = matchServiceImpl.getMatches();

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        assertEquals("Johan", dtos.get(0).getFirstPlayer().getName());
        assertEquals("Kohan", dtos.get(1).getWinnerPlayer().getName());
    }

    @Test
    void testGetMatchesByPlayerNameFound() {
        Player player1 = new Player(1L, "Johan");
        Player player2 = new Player(2L, "Pohan");
        Player player3 = new Player(3L, "Kohan");

        Match match1 = new Match(1L, player1, player2, player1);
        Match match2 = new Match(2L, player1, player3, player3);

        when(mockMatchRepository.getMatchesByPlayerName("Kohan")).thenReturn(List.of(match2));

        List<MatchDto> dtos = matchServiceImpl.getMatchesByPlayerName("Kohan");

        assertNotNull(dtos, "List DTO needs to be not null");
        assertEquals(1, dtos.size(), "Expect 1 match");
        assertEquals("Kohan", dtos.get(0).getWinnerPlayer().getName(), "Winner name must be 'Kohan'");
    }

    //TODO: если список пустой, исключение?
//    @Test
//    void testGetMatchesByPlayerNameNotFound() {
//        when(mockRepository.getMatchesByPlayerName("NonExisting")).thenReturn(Collections.emptyList());
//
//        List<MatchDto> dtos = matchServiceImpl.getMatchesByPlayerName("NonExisting");
//
//        assertNotNull(dtos, "List DTO needs to be not null");
//        assertTrue(dtos.isEmpty(), );
//    }
}