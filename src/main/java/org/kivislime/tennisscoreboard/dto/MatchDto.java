package org.kivislime.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchDto {
    Long id;
    PlayerDto firstPlayer;
    PlayerDto secondPlayer;
    PlayerDto winnerPlayer;
}
