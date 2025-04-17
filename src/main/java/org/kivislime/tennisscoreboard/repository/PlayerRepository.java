package org.kivislime.tennisscoreboard.repository;

import org.kivislime.tennisscoreboard.domain.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findByName(String name);

    Player addPlayer(Player player);
}
