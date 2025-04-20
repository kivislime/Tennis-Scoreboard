package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.domain.PlayerNumber;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;

import java.util.UUID;

public interface LiveMatchService {
    UUID createLiveMatchSession(String firstPlayerName, String secondPlayerName);

    MatchScoreDto getLiveMatchScore(String matchUuid);

    MatchScoreDto handleScoring(String matchId, PlayerNumber playerNumber);
}
