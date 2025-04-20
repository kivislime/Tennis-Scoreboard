package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.Match;

import java.util.List;

public interface MatchRepository {
    Match persist(Match match);

    List<Match> findAll(Integer pageNumber);

    List<Match> findByPlayerName(String playerName, Integer pageNumber);

    long count();

    long countByPlayerName(String playerName);
}
