package org.kivislime.tennisscoreboard.match;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.kivislime.tennisscoreboard.player.PlayerDto;

@Value
@Builder
@Jacksonized
public class MatchDto {
    Long id;
    PlayerDto firstPlayer;
    PlayerDto secondPlayer;
    PlayerDto winnerPlayer;
}
