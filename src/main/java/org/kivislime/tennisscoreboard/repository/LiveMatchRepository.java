package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.MatchScore;

import java.util.UUID;

public interface LiveMatchRepository {
    MatchScore persist(UUID uuid, MatchScore matchScore);

    MatchScore remove(UUID uuid);

    MatchScore getByUuid(UUID id);

    long totalLiveMatches();
}
