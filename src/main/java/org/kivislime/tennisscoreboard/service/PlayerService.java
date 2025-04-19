package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.dto.PlayerDto;

import java.util.Optional;

public interface PlayerService {
    PlayerDto addPlayer(String playerName);

    Optional<PlayerDto> getPlayer(String firstPlayerName);

    PlayerDto findOrCreatePlayer(String playerName);
}
