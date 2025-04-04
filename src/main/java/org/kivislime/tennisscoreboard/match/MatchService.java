package org.kivislime.tennisscoreboard.match;

import java.util.List;
import java.util.UUID;

public interface MatchService {
    List<MatchDto> getMatches();

    List<MatchDto> getMatchesByPlayerName(String playerName);

    UUID createLiveMatchSession(String firstPlayerName, String secondPlayerName);

    MatchScoreDto getLiveMatchScore(String matchUuid);

    MatchScoreDto increaseMatchScore(String matchId, String playerName);
}
