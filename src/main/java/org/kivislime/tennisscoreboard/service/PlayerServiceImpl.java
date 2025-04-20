package org.kivislime.tennisscoreboard.service;

import org.hibernate.exception.ConstraintViolationException;
import org.kivislime.tennisscoreboard.domain.Player;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.kivislime.tennisscoreboard.exception.PlayerRepositoryException;
import org.kivislime.tennisscoreboard.mapper.PlayerMapper;
import org.kivislime.tennisscoreboard.repository.PlayerRepository;

import java.util.Optional;

public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;


    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerDto persist(String playerName) {
        Player playerResult = playerRepository.persist(Player.builder()
                .name(playerName)
                .build());

        return playerMapper.playerToDto(playerResult);
    }

    @Override
    public Optional<PlayerDto> findPlayer(String playerName) {
        return playerRepository.findByName(playerName)
                .map(playerMapper::playerToDto);
    }

    @Override
    public PlayerDto findOrCreatePlayer(String playerName) {
        Optional<PlayerDto> playerDtoOptional = findPlayer(playerName);
        if (playerDtoOptional.isPresent()) {
            return playerDtoOptional.get();
        }

        try {
            Player player = playerRepository.persist(Player.builder()
                    .name(playerName)
                    .build());
            return playerMapper.playerToDto(player);
        } catch (ConstraintViolationException e) {
            Player existing = playerRepository.findByName(playerName)
                    .orElseThrow(() -> new PlayerRepositoryException("Failed to find player after constraint violation", e));

            return playerMapper.playerToDto(existing);
        }
    }
}
