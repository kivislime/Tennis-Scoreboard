package org.kivislime.tennisscoreboard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.config.MatchConstants;
import org.kivislime.tennisscoreboard.domain.MatchScore;
import org.kivislime.tennisscoreboard.domain.PlayerNumber;
import org.kivislime.tennisscoreboard.domain.PlayerScore;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.kivislime.tennisscoreboard.exception.MatchScoreException;
import org.kivislime.tennisscoreboard.exception.MaxGamesExceededException;
import org.kivislime.tennisscoreboard.repository.LiveMatchRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

        when(liveMatchRepository.persist(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(1)); // возвращаем матч как есть

        UUID uuid = liveMatchService.createLiveMatchSession("Alice", "Bob");

        assertNotNull(uuid);
    }

    @Test
    void getLiveMatchScore_shouldReturnDto() {
        UUID matchId = UUID.randomUUID();

        MatchScore matchScore = MatchScore.builder()
                .firstPlayerScore(new PlayerScore())
                .secondPlayerScore(new PlayerScore())
                .matchDto(MatchDto.builder()
                        .firstPlayer(PlayerDto.builder().name("A").build())
                        .secondPlayer(PlayerDto.builder().name("B").build())
                        .build())
                .build();

        when(liveMatchRepository.findByUuid(matchId)).thenReturn(matchScore);

        var result = liveMatchService.getLiveMatchScore(matchId.toString());

        assertNotNull(result);
        assertEquals("A", result.getMatchDto().getFirstPlayer().getName());
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

        MatchDto dto = MatchDto.builder()
                .firstPlayer(PlayerDto.builder().name("Alice").build())
                .secondPlayer(PlayerDto.builder().name("Bob").build())
                .build();

        MatchScore matchScore = MatchScore.builder()
                .matchDto(dto)
                .firstPlayerScore(first)
                .secondPlayerScore(second)
                .build();

        when(liveMatchRepository.findByUuid(id)).thenReturn(matchScore);
        when(liveMatchRepository.removeByUuid(id)).thenReturn(matchScore);

        assertThrows(MaxGamesExceededException.class, () ->
                liveMatchService.handleScoring(id.toString(), PlayerNumber.FIRST)
        );
    }

    @Test
    void handleScoring_shouldReturnFinishedMatch_whenFirstPlayerWins() {
        UUID id = UUID.randomUUID();

        MatchDto dto = MatchDto.builder()
                .firstPlayer(PlayerDto.builder().name("A").build())
                .secondPlayer(PlayerDto.builder().name("B").build())
                .build();

        PlayerScore firstScore = new PlayerScore();
        firstScore.winSet();
        firstScore.winSet();
        PlayerScore secondScore = new PlayerScore();

        MatchScore matchScore = MatchScore.builder()
                .matchDto(dto)
                .firstPlayerScore(firstScore)
                .secondPlayerScore(secondScore)
                .build();

        when(liveMatchRepository.findByUuid(id)).thenReturn(matchScore);
        when(finishedMatchService.persistFinishedMatch(any())).thenReturn(dto);

        var result = liveMatchService.handleScoring(id.toString(), PlayerNumber.FIRST);

        assertNotNull(result.getMatchDto());
        verify(liveMatchRepository).removeByUuid(id);
    }
}
