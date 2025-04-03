package org.kivislime.tennisscoreboard.player;

import org.kivislime.tennisscoreboard.MatchMapper;

import java.util.Optional;

public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    //TODO: MatchMapper include in all services? Util class?
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;


    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerDto addPlayer(String playerName) {
        //TODO: need to rework
        return null;
    }

    @Override
    public Optional<PlayerDto> getPlayer(String playerName) {
        return playerRepository.getPlayer(playerName)
                .map(matchMapper::playerToDto);
    }
}
