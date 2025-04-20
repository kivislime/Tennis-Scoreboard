package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.config.PaginationConfig;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.mapper.MatchMapper;
import org.kivislime.tennisscoreboard.repository.MatchRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MatchQueryServiceImpl implements MatchQueryService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public MatchQueryServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public List<MatchDto> findMatches(Integer pageNumber) {
        return matchRepository.findAll(pageNumber)
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countPages() {
        long totalMatches = matchRepository.count();
        return calculateTotalPages(totalMatches);
    }

    @Override
    public List<MatchDto> findMatchesByPlayer(String playerName, Integer pageNumber) {
        return matchRepository.findByPlayerName(playerName, pageNumber)
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countPagesByPlayer(String playerName) {
        long totalMatches = matchRepository.countByPlayerName(playerName);
        return calculateTotalPages(totalMatches);
    }

    private long calculateTotalPages(long totalMatches) {
        return (int) Math.ceil((double) totalMatches / PaginationConfig.PAGE_SIZE);
    }
}
