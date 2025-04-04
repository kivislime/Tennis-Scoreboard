package org.kivislime.tennisscoreboard.match;

import org.kivislime.tennisscoreboard.MatchMapper;
import org.kivislime.tennisscoreboard.player.PlayerDto;
import org.kivislime.tennisscoreboard.player.PlayerService;

import java.util.*;
import java.util.stream.Collectors;

public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;
    //TODO: or Map<uuid,Match> - ?
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

        MatchScore matchScore = new MatchScore(newMatch, 0L, 0L);

        currentMatches.put(uuid, matchScore);

        return uuid;
    }

    @Override
    public MatchScoreDto getLiveMatchScore(String matchUuid) {
        UUID uuid = UUID.fromString(matchUuid);
        return Optional.ofNullable(currentMatches.get(uuid))
                .map(matchMapper::scoreToDto)
                .orElseThrow(() -> new RuntimeException("Match score not found for id: " + matchUuid));
    }

    @Override
    public MatchScoreDto increaseMatchScore(String matchId, String playerName) {
        MatchScore matchScore = currentMatches.get(UUID.fromString(matchId));

        if (matchScore == null) {
            throw new RuntimeException("Match score not found for id: " + matchId);
        }

        MatchDto match = matchScore.getMatch();
        //TODO: изменить логику проверок. Какие параметры? Правильно ли соотносятся игрок 1 и 2 в по матчу
        // и тут в incrementFirstPlayerScore incrementSecondPlayerScore? Не будет ли первый метод увеличивать второму игроку счет?
        if (playerName.contains("player1")) {
            matchScore.incrementFirstPlayerScore();
        } else if (playerName.contains("player2")) {
            matchScore.incrementSecondPlayerScore();
        } else {
            throw new RuntimeException("Player name" + playerName + " not found in match id: : " + matchId);
        }
        //TODO: if match end logic
        return matchMapper.scoreToDto(matchScore);
    }
}
