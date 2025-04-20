package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.domain.Match;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.mapper.MatchMapper;
import org.kivislime.tennisscoreboard.repository.MatchRepository;

public class FinishedMatchServiceImpl implements FinishedMatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public FinishedMatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public MatchDto persistFinishedMatch(MatchDto build) {
        Match match = matchRepository.addMatch(matchMapper.dtoToMatch(build));
        return matchMapper.matchToDto(match);
    }
}
