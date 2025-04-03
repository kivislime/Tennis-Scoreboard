package org.kivislime.tennisscoreboard.match;

import java.util.List;

public interface MatchRepository {
    List<Match> getMatches();

    List<Match> getMatchesByPlayerName(String playerName);
}
