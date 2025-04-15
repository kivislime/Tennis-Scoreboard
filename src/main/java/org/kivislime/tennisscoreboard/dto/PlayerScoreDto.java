package org.kivislime.tennisscoreboard.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PlayerScoreDto {
    Integer sets;
    Integer games;
    Integer points;
}
