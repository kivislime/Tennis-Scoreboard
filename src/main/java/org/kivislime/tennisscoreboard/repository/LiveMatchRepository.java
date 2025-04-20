package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.MatchScore;

import java.util.UUID;

public interface LiveMatchRepository {
    MatchScore persist(UUID uuid, MatchScore matchScore);

    MatchScore findByUuid(UUID id);

    MatchScore removeByUuid(UUID uuid);

    long count();
}
