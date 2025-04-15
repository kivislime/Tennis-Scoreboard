package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> getPlayer(String name);

    Player addPlayer(Player player);
}
