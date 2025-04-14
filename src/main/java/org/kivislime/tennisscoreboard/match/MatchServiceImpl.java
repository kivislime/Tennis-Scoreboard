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
    private static final int MAX_GAMES_IN_SET = 50;

    private static final Map<UUID, MatchScore> currentMatches = Collections.synchronizedMap(new HashMap<>());
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
                .orElseThrow(() -> new MatchScoreException("Match score not found for id: " + matchUuid));
    }

    @Override
    public MatchScoreDto handleScoring(String matchId, PlayerNumber playerNumber) {
        UUID uuid = UUID.fromString(matchId);
        MatchScore matchScore = currentMatches.get(uuid);

        if (matchScore == null) {
            throw new MatchScoreException("Match score not found for id: " + uuid);
        }

        MatchDto match = matchScore.getMatchDto();
        PlayerScore firstPlayerScore = matchScore.getFirstPlayerScore();
        PlayerScore secondPlayerScore = matchScore.getSecondPlayerScore();

        matchScore.processPointWinner(playerNumber);

        if (matchScore.isMaxGames(MAX_GAMES_IN_SET)) {
            currentMatches.remove(uuid);
            throw new MaxGamesExceededException("Maximum number of games. The match reached the limit of games in the set");
        }

        //TODO: где хранить все константы? Логически расположить где? То что в конфиге понятно, вопрос тоже в каком спросить, класс или файл
        if (firstPlayerScore.getSets() >= MatchScore.MAX_SETS_FOR_WIN) {
            currentMatches.remove(uuid);
            return buildFinishedMatchScoreDto(match, PlayerNumber.FIRST, firstPlayerScore, secondPlayerScore);
        } else if (secondPlayerScore.getSets() >= MatchScore.MAX_SETS_FOR_WIN) {
            currentMatches.remove(uuid);
            return buildFinishedMatchScoreDto(match, PlayerNumber.SECOND, firstPlayerScore, secondPlayerScore);
        }

        return matchMapper.matchScoreToDto(matchScore);
    }

    private MatchScoreDto buildFinishedMatchScoreDto(MatchDto match, PlayerNumber winner, PlayerScore first, PlayerScore second) {
        PlayerDto firstPlayerDto = playerService.addPlayer(match.getFirstPlayer().getName());
        PlayerDto secondPlayerDto = playerService.addPlayer(match.getSecondPlayer().getName());

        Player firstPlayer = playerMapper.playerDtoToPlayer(firstPlayerDto);
        Player secondPlayer = playerMapper.playerDtoToPlayer(secondPlayerDto);

        Match resultMatch = matchRepository.addMatch(new Match(null,
                firstPlayer,
                secondPlayer,
                winner == PlayerNumber.FIRST ? firstPlayer : secondPlayer));


        MatchDto resultMatchDto = matchMapper.matchToDto(resultMatch);

        return new MatchScoreDto(resultMatchDto,
                playerScoreMapper.playerScoreToDto(first),
                playerScoreMapper.playerScoreToDto(second));
    }

    private long calculateTotalPages(long totalMatches) {
        return (int) Math.ceil((double) totalMatches / PaginationConfig.PAGE_SIZE);
    }

    MatchScore getMatchScore(UUID id) {
        return currentMatches.get(id);
    }
}
