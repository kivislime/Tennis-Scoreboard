package org.kivislime.tennisscoreboard.match;

import org.kivislime.tennisscoreboard.MatchMapper;
import org.kivislime.tennisscoreboard.PlayerMapper;
import org.kivislime.tennisscoreboard.PlayerScoreMapper;
import org.kivislime.tennisscoreboard.player.Player;
import org.kivislime.tennisscoreboard.player.PlayerDto;
import org.kivislime.tennisscoreboard.player.PlayerService;

import java.util.*;
import java.util.stream.Collectors;

public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;
    private final PlayerScoreMapper playerScoreMapper = PlayerScoreMapper.INSTANCE;

    private static Map<UUID, MatchScore> currentMatches = Collections.synchronizedMap(new HashMap<>());
    private final PlayerService playerService;


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

    @Override
    public UUID createLiveMatchSession(String firstPlayerName, String secondPlayerName) {

        Optional<PlayerDto> firstPlayerOptional = playerService.getPlayer(firstPlayerName);
        Optional<PlayerDto> secondPlayerOptional = playerService.getPlayer(secondPlayerName);

        PlayerDto firstPlayerDto = firstPlayerOptional.orElseGet(() ->
                PlayerDto.builder()
                        .name(firstPlayerName)
                        .build());

        PlayerDto secondPlayerDto = secondPlayerOptional.orElseGet(() ->
                PlayerDto.builder()
                        .name(secondPlayerName)
                        .build());

        UUID uuid = UUID.randomUUID();
        MatchDto newMatch = new MatchDto(null, firstPlayerDto, secondPlayerDto, null);

        MatchScore matchScore = new MatchScore(new PlayerScore(), new PlayerScore(), newMatch);

        currentMatches.put(uuid, matchScore);

        return uuid;
    }

    @Override
    public MatchScoreDto getLiveMatchScore(String matchUuid) {
        UUID uuid = UUID.fromString(matchUuid);
        return Optional.ofNullable(currentMatches.get(uuid))
                .map(matchMapper::matchScoreToDto)
                .orElseThrow(() -> new RuntimeException("Match score not found for id: " + matchUuid));
    }

    @Override
    public MatchScoreDto handleScoring(String matchId, Integer playerNumber) {
        MatchScore matchScore = currentMatches.get(UUID.fromString(matchId));

        if (matchScore == null) {
            throw new RuntimeException("Match score not found for id: " + matchId);
        }

        //TODO: изменить логику проверок. Какие параметры? Правильно ли соотносятся игрок 1 и 2 в по матчу
        MatchDto match = matchScore.getMatchDto();
        PlayerScore firstPlayerScore = matchScore.getFirstPlayerScore();
        PlayerScore secondPlayerScore = matchScore.getSecondPlayerScore();

        matchScore.processPointWinner(playerNumber);

        if (firstPlayerScore.getSets() >= MatchScore.MAX_SETS_FOR_WIN) {
            return buildFinishedMatchScoreDto(match, match.getFirstPlayer(), firstPlayerScore, secondPlayerScore);
        } else if (secondPlayerScore.getSets() >= MatchScore.MAX_SETS_FOR_WIN) {
            return buildFinishedMatchScoreDto(match, match.getSecondPlayer(), firstPlayerScore, secondPlayerScore);
        }

        return matchMapper.matchScoreToDto(matchScore);
    }

    private MatchScoreDto buildFinishedMatchScoreDto(MatchDto match, PlayerDto winner, PlayerScore first, PlayerScore second) {
        MatchDto resultMatchDto = new MatchDto(null, match.getFirstPlayer(), match.getSecondPlayer(), winner);
        return new MatchScoreDto(resultMatchDto,
                playerScoreMapper.playerScoreToDto(first),
                playerScoreMapper.playerScoreToDto(second));
    }

}

