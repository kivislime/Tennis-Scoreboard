package org.kivislime.tennisscoreboard.match;

import org.kivislime.tennisscoreboard.MatchMapper;
import org.kivislime.tennisscoreboard.player.PlayerDto;
import org.kivislime.tennisscoreboard.player.PlayerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;


    public MatchServiceImpl(MatchRepository matchRepository, PlayerService playerService) {
        this.matchRepository = matchRepository;
        this.playerService = playerService;
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
