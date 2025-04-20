package org.kivislime.tennisscoreboard.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.domain.Match;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.repository.MatchRepository;
import org.kivislime.tennisscoreboard.service.MatchQueryServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchQueryServiceImplTest {

    private MatchRepository matchRepository;
    private MatchQueryServiceImpl matchService;

    @BeforeEach
    void setUp() {
        matchRepository = mock(MatchRepository.class);
        matchService = new MatchQueryServiceImpl(matchRepository);
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
