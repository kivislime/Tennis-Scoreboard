package org.kivislime.tennisscoreboard;

import java.util.List;
import java.util.stream.Collectors;

public class MatchService {
    private final MatchRepositoryImpl matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public MatchService(MatchRepositoryImpl matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchDto> getMatches() {
        return matchRepository.getMatches()
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }
}
