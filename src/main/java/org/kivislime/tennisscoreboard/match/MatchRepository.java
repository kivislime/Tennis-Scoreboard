package org.kivislime.tennisscoreboard.match;

import java.util.List;

public interface MatchRepository {
    List<Match> getMatches(Integer pageNumber);

    long getTotalMatches();

    List<Match> getMatchesByPlayerName(String playerName, Integer pageNumber);

    long getTotalMatchesByPlayerName(String playerName);

    Match addMatch(Match match);

}
