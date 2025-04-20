package org.kivislime.tennisscoreboard.service;

import lombok.extern.slf4j.Slf4j;
import org.kivislime.tennisscoreboard.config.MatchConstants;
import org.kivislime.tennisscoreboard.domain.MatchScore;
import org.kivislime.tennisscoreboard.domain.PlayerNumber;
import org.kivislime.tennisscoreboard.domain.PlayerScore;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.kivislime.tennisscoreboard.exception.MatchScoreException;
import org.kivislime.tennisscoreboard.exception.MaxGamesExceededException;
import org.kivislime.tennisscoreboard.mapper.MatchMapper;
import org.kivislime.tennisscoreboard.mapper.PlayerMapper;
import org.kivislime.tennisscoreboard.mapper.PlayerScoreMapper;
import org.kivislime.tennisscoreboard.repository.LiveMatchRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class LiveMatchServiceImpl implements LiveMatchService {
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;
    private final PlayerScoreMapper playerScoreMapper = PlayerScoreMapper.INSTANCE;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    private final PlayerService playerService;
    private final LiveMatchRepository liveMatchRepository;
    private final FinishedMatchService finishedMatchService;

    public LiveMatchServiceImpl(PlayerService playerService, FinishedMatchService finishedMatchService, LiveMatchRepository liveMatchRepository) {
        this.playerService = playerService;
        this.finishedMatchService = finishedMatchService;
        this.liveMatchRepository = liveMatchRepository;
    }

    public MatchScore getMatchScore(UUID id) {
        return liveMatchRepository.getByUuid(id);
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

        MatchScore currentScore = liveMatchRepository.persist(uuid, matchScore);

        log.info("New live match created: : {}", currentScore.toString());
        log.info("Current size of lives matches: {}", liveMatchRepository.totalLiveMatches());

        return uuid;
    }

    @Override
    public MatchScoreDto getLiveMatchScore(String matchUuid) {
        UUID uuid = UUID.fromString(matchUuid);
        return Optional.ofNullable(liveMatchRepository.getByUuid(uuid))
                .map(matchMapper::matchScoreToDto)
                .orElseThrow(() -> new MatchScoreException(String.format("Match score not found for id: %s", matchUuid)));
    }

    @Override
    public MatchScoreDto handleScoring(String matchId, PlayerNumber playerNumber) {
        UUID uuid = UUID.fromString(matchId);
        MatchScore matchScore = liveMatchRepository.getByUuid(uuid);

        if (matchScore == null) {
            throw new MatchScoreException(String.format("Match score not found for id: %s", uuid));
        }

        MatchDto match = matchScore.getMatchDto();
        PlayerScore firstPlayerScore = matchScore.getFirstPlayerScore();
        PlayerScore secondPlayerScore = matchScore.getSecondPlayerScore();

        matchScore.processPointWinner(playerNumber);

        if (matchScore.isMaxGames()) {
            MatchScore matchScoreException = liveMatchRepository.remove(uuid);
            throw new MaxGamesExceededException(String.format("Maximum number of games. The match reached the limit of games in the set: %s", matchScoreException.toString()));
        }

        if (firstPlayerScore.getSets() >= MatchConstants.MAX_SETS_FOR_WIN) {
            liveMatchRepository.remove(uuid);
            return buildFinishedMatchScoreDto(match, PlayerNumber.FIRST, firstPlayerScore, secondPlayerScore);
        } else if (secondPlayerScore.getSets() >= MatchConstants.MAX_SETS_FOR_WIN) {
            liveMatchRepository.remove(uuid);
            return buildFinishedMatchScoreDto(match, PlayerNumber.SECOND, firstPlayerScore, secondPlayerScore);
        }

        return matchMapper.matchScoreToDto(matchScore);
    }

    private MatchScoreDto buildFinishedMatchScoreDto(MatchDto match, PlayerNumber winner, PlayerScore first, PlayerScore second) {

        PlayerDto firstPlayer = playerService.findOrCreatePlayer(match.getFirstPlayer().getName());
        PlayerDto secondPlayer = playerService.findOrCreatePlayer(match.getSecondPlayer().getName());

        MatchDto resultMatch = finishedMatchService.persistFinishedMatch(MatchDto.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .winnerPlayer(winner == PlayerNumber.FIRST ? firstPlayer : secondPlayer)
                .build());

        return MatchScoreDto.builder()
                .matchDto(resultMatch)
                .firstPlayerScore(playerScoreMapper.playerScoreToDto(first))
                .secondPlayerScore(playerScoreMapper.playerScoreToDto(second))
                .build();
    }
}
