package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.dto.MatchDto;

import java.util.List;

public interface MatchQueryService {
    List<MatchDto> findMatches(Integer pageNumber);

    long countPages();

    List<MatchDto> findMatchesByPlayer(String playerName, Integer pageNumber);

    long countPagesByPlayer(String playerName);
}
