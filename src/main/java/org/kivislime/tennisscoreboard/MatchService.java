package org.kivislime.tennisscoreboard;

import java.util.List;

public interface MatchService {
    List<MatchDto> getMatches();

    List<MatchDto> getMatchesByPlayerName(String playerName);
}
