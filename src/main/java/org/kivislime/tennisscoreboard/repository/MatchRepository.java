package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.Match;

import java.util.List;

public interface MatchRepository {
    List<Match> getMatches(Integer pageNumber);

    long getTotalMatches();

    List<Match> getMatchesByPlayerName(String playerName, Integer pageNumber);

    long getTotalMatchesByPlayerName(String playerName);

    Match addMatch(Match match);
}
