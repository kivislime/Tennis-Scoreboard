package org.kivislime.tennisscoreboard.match;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchScoreDto {
    MatchDto matchDto;
    PlayerScoreDto firstPlayerScore;
    PlayerScoreDto secondPlayerScore;
}
