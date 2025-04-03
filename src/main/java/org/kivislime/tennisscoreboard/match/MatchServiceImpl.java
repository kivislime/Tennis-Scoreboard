package org.kivislime.tennisscoreboard.match;

import org.kivislime.tennisscoreboard.MatchMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchDto> getMatches() {
        return matchRepository.getMatches()
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }

    public List<MatchDto> getMatchesByPlayerName(String playerName) {
        return matchRepository.getMatchesByPlayerName(playerName)
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }
}
