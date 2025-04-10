package org.kivislime.tennisscoreboard.player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> getPlayer(String name);

    Player addPlayer(Player player);
}
