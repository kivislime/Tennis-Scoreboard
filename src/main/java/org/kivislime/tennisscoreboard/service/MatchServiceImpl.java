package org.kivislime.tennisscoreboard.service;

import lombok.extern.slf4j.Slf4j;
import org.kivislime.tennisscoreboard.config.MatchConstants;
import org.kivislime.tennisscoreboard.config.PaginationConfig;
import org.kivislime.tennisscoreboard.domain.*;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.kivislime.tennisscoreboard.exception.MatchScoreException;
import org.kivislime.tennisscoreboard.exception.MaxGamesExceededException;
import org.kivislime.tennisscoreboard.mapper.MatchMapper;
import org.kivislime.tennisscoreboard.mapper.PlayerMapper;
import org.kivislime.tennisscoreboard.mapper.PlayerScoreMapper;
import org.kivislime.tennisscoreboard.repository.MatchRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;
    private final PlayerScoreMapper playerScoreMapper = PlayerScoreMapper.INSTANCE;

    private static final Map<UUID, MatchScore> currentMatches = new ConcurrentHashMap<>();
    private final PlayerService playerService;

    public MatchServiceImpl(MatchRepository matchRepository, PlayerService playerService) {
        this.matchRepository = matchRepository;
        this.playerService = playerService;
    }

    @Override
    public List<MatchDto> getMatches(Integer pageNumber) {
        return matchRepository.getMatches(pageNumber)
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalPages() {
        long totalMatches = matchRepository.getTotalMatches();
        return calculateTotalPages(totalMatches);
    }

    @Override
    public List<MatchDto> getMatchesByPlayerName(String playerName, Integer pageNumber) {
        return matchRepository.getMatchesByPlayerName(playerName, pageNumber)
                .stream()
                .map(matchMapper::matchToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalPagesByPlayerName(String playerName) {
        long totalMatches = matchRepository.getTotalMatchesByPlayerName(playerName);
        return calculateTotalPages(totalMatches);
    }

    @Override
    public UUID createLiveMatchSession(String firstPlayerName, String secondPlayerName) {
        PlayerDto firstPlayerDto = PlayerDto.builder()
                .name(firstPlayerName)
                .build();

        PlayerDto secondPlayerDto = PlayerDto.builder()
                .name(secondPlayerName)
                .build();

        UUID uuid = UUID.randomUUID();

        MatchDto newMatch = MatchDto.builder()
                .firstPlayer(firstPlayerDto)
                .secondPlayer(secondPlayerDto)
                .build();

        MatchScore matchScore = MatchScore.builder().firstPlayerScore(new PlayerScore())
                .secondPlayerScore(new PlayerScore())
                .matchDto(newMatch)
                .build();

        currentMatches.put(uuid, matchScore);

        log.info("New live match created, UUID: {}", uuid);
        log.info("Current size of lives matches: {}", currentMatches.size());

        return uuid;
    }

    @Override
    public MatchScoreDto getLiveMatchScore(String matchUuid) {
        UUID uuid = UUID.fromString(matchUuid);
        return Optional.ofNullable(currentMatches.get(uuid))
                .map(matchMapper::matchScoreToDto)
                .orElseThrow(() -> new MatchScoreException(String.format("Match score not found for id: %s", matchUuid)));
    }

    @Override
    public MatchScoreDto handleScoring(String matchId, PlayerNumber playerNumber) {
        UUID uuid = UUID.fromString(matchId);
        MatchScore matchScore = currentMatches.get(uuid);

        if (matchScore == null) {
            throw new MatchScoreException(String.format("Match score not found for id: %s", uuid));
        }

        MatchDto match = matchScore.getMatchDto();
        PlayerScore firstPlayerScore = matchScore.getFirstPlayerScore();
        PlayerScore secondPlayerScore = matchScore.getSecondPlayerScore();

        matchScore.processPointWinner(playerNumber);

        if (matchScore.isMaxGames()) {
            currentMatches.remove(uuid);
            throw new MaxGamesExceededException("Maximum number of games. The match reached the limit of games in the set");
        }

        if (firstPlayerScore.getSets() >= MatchConstants.MAX_SETS_FOR_WIN) {
            currentMatches.remove(uuid);
            return buildFinishedMatchScoreDto(match, PlayerNumber.FIRST, firstPlayerScore, secondPlayerScore);
        } else if (secondPlayerScore.getSets() >= MatchConstants.MAX_SETS_FOR_WIN) {
            currentMatches.remove(uuid);
            return buildFinishedMatchScoreDto(match, PlayerNumber.SECOND, firstPlayerScore, secondPlayerScore);
        }

        return matchMapper.matchScoreToDto(matchScore);
    }

    private MatchScoreDto buildFinishedMatchScoreDto(MatchDto match, PlayerNumber winner, PlayerScore first, PlayerScore second) {

        PlayerDto firstPlayerDto = playerService.findOrCreatePlayer(match.getFirstPlayer().getName());
        PlayerDto secondPlayerDto = playerService.findOrCreatePlayer(match.getSecondPlayer().getName());

        Player firstPlayer = playerMapper.playerDtoToPlayer(firstPlayerDto);
        Player secondPlayer = playerMapper.playerDtoToPlayer(secondPlayerDto);

        Match resultMatch = matchRepository.addMatch(Match.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .winnerPlayer(winner == PlayerNumber.FIRST ? firstPlayer : secondPlayer)
                .build());

        MatchDto resultMatchDto = matchMapper.matchToDto(resultMatch);

        return MatchScoreDto.builder()
                .matchDto(resultMatchDto)
                .firstPlayerScore(playerScoreMapper.playerScoreToDto(first))
                .secondPlayerScore(playerScoreMapper.playerScoreToDto(second))
                .build();
    }

    private long calculateTotalPages(long totalMatches) {
        return (int) Math.ceil((double) totalMatches / PaginationConfig.PAGE_SIZE);
    }

    public MatchScore getMatchScore(UUID id) {
        return currentMatches.get(id);
    }
}
