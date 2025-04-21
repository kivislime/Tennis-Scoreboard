package org.kivislime.tennisscoreboard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.config.MatchConstants;
import org.kivislime.tennisscoreboard.domain.*;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.kivislime.tennisscoreboard.exception.MatchScoreException;
import org.kivislime.tennisscoreboard.exception.MaxGamesExceededException;
import org.kivislime.tennisscoreboard.repository.LiveMatchRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LiveMatchServiceImplTest {

    private PlayerService playerService;
    private FinishedMatchService finishedMatchService;
    private LiveMatchRepository liveMatchRepository;
    private LiveMatchServiceImpl liveMatchService;

    @BeforeEach
    void setUp() {
        playerService = mock(PlayerService.class);
        finishedMatchService = mock(FinishedMatchService.class);
        liveMatchRepository = mock(LiveMatchRepository.class);
        liveMatchService = new LiveMatchServiceImpl(playerService, finishedMatchService, liveMatchRepository);
    }

    @Test
    void createLiveMatchSession_shouldReturnUuid() {
        when(liveMatchRepository.persist(any(UUID.class), any(MatchScore.class)))
                .thenAnswer(inv -> inv.getArgument(1));

        UUID uuid = liveMatchService.createLiveMatchSession("Alice", "Bob");

        assertNotNull(uuid);
    }

    @Test
    void getLiveMatchScore_shouldReturnDto() {
        UUID matchId = UUID.randomUUID();
        Player p1 = Player.builder().name("A").build();
        Player p2 = Player.builder().name("B").build();
        Match domainMatch = Match.builder()
                .firstPlayer(p1)
                .secondPlayer(p2)
                .build();

        MatchScore matchScore = MatchScore.builder()
                .firstPlayerScore(new PlayerScore())
                .secondPlayerScore(new PlayerScore())
                .match(domainMatch)
                .build();

        when(liveMatchRepository.findByUuid(matchId)).thenReturn(matchScore);

        MatchScoreDto result = liveMatchService.getLiveMatchScore(matchId.toString());

        assertNotNull(result);
        assertEquals("A", result.getMatch().getFirstPlayer().getName());
    }

    @Test
    void getLiveMatchScore_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(liveMatchRepository.findByUuid(id)).thenReturn(null);

        assertThrows(MatchScoreException.class,
                () -> liveMatchService.getLiveMatchScore(id.toString()));
    }

    @Test
    void handleScoring_shouldThrowIfMatchNotFound() {
        UUID id = UUID.randomUUID();
        when(liveMatchRepository.findByUuid(id)).thenReturn(null);

        assertThrows(MatchScoreException.class,
                () -> liveMatchService.handleScoring(id.toString(), PlayerNumber.FIRST));
    }

    @Test
    void handleScoring_shouldThrowMaxGamesExceeded() {
        UUID id = UUID.randomUUID();

        PlayerScore first = new PlayerScore();
        PlayerScore second = new PlayerScore();
        for (int i = 0; i <= MatchConstants.MAX_GAMES_IN_SET; i++) {
            first.winGame();
            second.winGame();
        }

        Player p1 = Player.builder().name("Alice").build();
        Player p2 = Player.builder().name("Bob").build();
        Match domainMatch = Match.builder().firstPlayer(p1).secondPlayer(p2).build();

        MatchScore matchScore = MatchScore.builder()
                .match(domainMatch)
                .firstPlayerScore(first)
                .secondPlayerScore(second)
                .build();

        when(liveMatchRepository.findByUuid(id)).thenReturn(matchScore);
        when(liveMatchRepository.removeByUuid(id)).thenReturn(matchScore);

        assertThrows(MaxGamesExceededException.class,
                () -> liveMatchService.handleScoring(id.toString(), PlayerNumber.FIRST));
    }

    @Test
    void handleScoring_shouldReturnFinishedMatch_whenFirstPlayerWins() {
        UUID id = UUID.randomUUID();

        PlayerScore firstScore = new PlayerScore();
        firstScore.winSet();
        firstScore.winSet();
        PlayerScore secondScore = new PlayerScore();

        Player p1 = Player.builder().name("A").build();
        Player p2 = Player.builder().name("B").build();
        Match domainMatch = Match.builder().firstPlayer(p1).secondPlayer(p2).build();

        MatchScore matchScore = MatchScore.builder()
                .match(domainMatch)
                .firstPlayerScore(firstScore)
                .secondPlayerScore(secondScore)
                .build();

        when(liveMatchRepository.findByUuid(id)).thenReturn(matchScore);
        MatchDto savedDto = MatchDto.builder()
                .firstPlayer(PlayerDto.builder().name("A").build())
                .secondPlayer(PlayerDto.builder().name("B").build())
                .winnerPlayer(PlayerDto.builder().name("A").build())
                .build();
        when(finishedMatchService.persistFinishedMatch(any())).thenReturn(savedDto);

        var result = liveMatchService.handleScoring(id.toString(), PlayerNumber.FIRST);

        assertNotNull(result.getMatch());
        assertEquals("A", result.getMatch().getWinnerPlayer().getName());
        verify(liveMatchRepository).removeByUuid(id);
    }
}
