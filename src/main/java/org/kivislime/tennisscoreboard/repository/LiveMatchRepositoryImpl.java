package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.MatchScore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LiveMatchRepositoryImpl implements LiveMatchRepository {
    private static final Map<UUID, MatchScore> currentMatches = new ConcurrentHashMap<>();

    @Override
    public MatchScore findByUuid(UUID id) {
        return currentMatches.get(id);
    }

    @Override
    public long count() {
        return currentMatches.size();
    }

    @Override
    public MatchScore removeByUuid(UUID uuid) {
        MatchScore score = currentMatches.get(uuid);
        currentMatches.remove(uuid);
        return score;
    }

    @Override
    public MatchScore persist(UUID uuid, MatchScore matchScore) {
        currentMatches.put(uuid, matchScore);
        return matchScore;
    }
}
