package org.kivislime.tennisscoreboard.player;

import java.util.Optional;

public interface PlayerService {
    PlayerDto addPlayer(String playerName);

    Optional<PlayerDto> getPlayer(String firstPlayerName);
}
