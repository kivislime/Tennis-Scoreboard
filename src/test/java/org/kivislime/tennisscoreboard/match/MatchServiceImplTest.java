package org.kivislime.tennisscoreboard.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.player.Player;
import org.kivislime.tennisscoreboard.player.PlayerDto;
import org.kivislime.tennisscoreboard.player.PlayerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceImplTest {

    private MatchRepository matchRepository;
    private PlayerService playerService;
    private MatchServiceImpl matchService;

    @BeforeEach
    void setUp() {
        matchRepository = mock(MatchRepository.class);
        playerService = mock(PlayerService.class);
        matchService = new MatchServiceImpl(matchRepository, playerService);
    }

    @Test
    void createLiveMatchSession_shouldCreateNewMatchIfPlayersNotExist() {
        String firstName = "Alice";
        String secondName = "Bob";

        when(playerService.getPlayer(firstName)).thenReturn(Optional.empty());
        when(playerService.getPlayer(secondName)).thenReturn(Optional.empty());

        UUID matchId = matchService.createLiveMatchSession(firstName, secondName);
        assertNotNull(matchId);

        MatchScoreDto matchScore = matchService.getLiveMatchScore(matchId.toString());
        assertEquals(firstName, matchScore.getMatchDto().getFirstPlayer().getName());
        assertEquals(secondName, matchScore.getMatchDto().getSecondPlayer().getName());

    }

    @Test
    void getLiveMatchScore_shouldThrowExceptionIfNotFound() {
        String fakeUuid = UUID.randomUUID().toString();
        MatchScoreException ex = assertThrows(MatchScoreException.class,
                () -> matchService.getLiveMatchScore(fakeUuid));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void handleScoring_shouldThrowIfMatchNotFound() {
        UUID uuid = UUID.randomUUID();
        MatchScoreException ex = assertThrows(MatchScoreException.class,
                () -> matchService.handleScoring(uuid.toString(), PlayerNumber.FIRST));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void handleScoring_shouldEndMatchWhenMaxSetsReached() {
        String first = "Player1";
        String second = "Player2";

        when(playerService.getPlayer(first)).thenReturn(Optional.empty());
        when(playerService.getPlayer(second)).thenReturn(Optional.empty());

        UUID matchId = matchService.createLiveMatchSession(first, second);
        MatchScore matchScore = matchService.getMatchScore(matchId);

        for (int i = 0; i < MatchScore.MAX_SETS_FOR_WIN; i++) {
            matchScore.getFirstPlayerScore().winSet();
        }

        PlayerDto playerDto1 = PlayerDto.builder().name(first).build();
        PlayerDto playerDto2 = PlayerDto.builder().name(second).build();

        when(playerService.addPlayer(first)).thenReturn(playerDto1);
        when(playerService.addPlayer(second)).thenReturn(playerDto2);

        Match fakeSavedMatch = new Match(null, new Player(), new Player(), new Player());
        when(matchRepository.addMatch(any())).thenReturn(fakeSavedMatch);

        MatchScoreDto result = matchService.handleScoring(matchId.toString(), PlayerNumber.FIRST);
        assertNotNull(result.getMatchDto());
    }

    @Test
    void getMatches_shouldReturnDtoList() {
        Match match = new Match();
        when(matchRepository.getMatches(1)).thenReturn(List.of(match));

        List<MatchDto> matches = matchService.getMatches(1);
        assertEquals(1, matches.size());
    }

    @Test
    void getTotalPages_shouldReturnCorrectPages() {
        when(matchRepository.getTotalMatches()).thenReturn(15L);
        long totalPages = matchService.getTotalPages();
        assertEquals(2, totalPages);
    }
}
