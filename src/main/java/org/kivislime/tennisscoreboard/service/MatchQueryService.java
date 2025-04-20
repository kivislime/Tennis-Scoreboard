package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.dto.MatchDto;

import java.util.List;

public interface MatchQueryService {
    List<MatchDto> getMatches(Integer pageNumber);

    long getTotalPages();

    List<MatchDto> getMatchesByPlayerName(String playerName, Integer pageNumber);

    long getTotalPagesByPlayerName(String playerName);
}
