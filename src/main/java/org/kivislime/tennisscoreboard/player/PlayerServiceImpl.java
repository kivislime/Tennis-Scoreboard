package org.kivislime.tennisscoreboard.player;

import org.kivislime.tennisscoreboard.PlayerMapper;

import java.util.Optional;

public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;


    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerDto addPlayer(String playerName) {
        Player playerResult = playerRepository.addPlayer(Player.builder()
                .name(playerName)
                .build());

        return playerMapper.playerToDto(playerResult);
    }

    @Override
    public Optional<PlayerDto> getPlayer(String playerName) {
        return playerRepository.getPlayer(playerName)
                .map(playerMapper::playerToDto);
    }
}
