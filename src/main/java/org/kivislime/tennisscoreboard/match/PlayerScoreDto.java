package org.kivislime.tennisscoreboard.match;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PlayerScoreDto {
    Integer sets;
    Integer games;
    Integer points;
}
