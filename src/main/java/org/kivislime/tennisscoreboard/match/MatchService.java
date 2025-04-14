package org.kivislime.tennisscoreboard.match;

import java.util.List;
import java.util.UUID;

public interface MatchService {
    List<MatchDto> getMatches(Integer pageNumber);

    long getTotalPages();

    List<MatchDto> getMatchesByPlayerName(String playerName, Integer pageNumber);

    long getTotalPagesByPlayerName(String playerName);

    UUID createLiveMatchSession(String firstPlayerName, String secondPlayerName);

    MatchScoreDto getLiveMatchScore(String matchUuid);

    MatchScoreDto handleScoring(String matchId, PlayerNumber playerNumber);

}
