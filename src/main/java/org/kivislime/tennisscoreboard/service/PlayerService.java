package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.dto.PlayerDto;

import java.util.Optional;

public interface PlayerService {
    PlayerDto persist(String playerName);

    Optional<PlayerDto> findPlayer(String firstPlayerName);

    PlayerDto findOrCreatePlayer(String playerName);
}
