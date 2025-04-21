package org.kivislime.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchScoreDto {
    MatchDto match;
    PlayerScoreDto firstPlayerScore;
    PlayerScoreDto secondPlayerScore;
}
