package org.kivislime.tennisscoreboard;

import java.util.List;

public interface MatchRepository {
    List<Match> getMatches();

    List<Match> getMatchesByPlayerName(String playerName);
}
