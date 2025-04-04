package org.kivislime.tennisscoreboard.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@AllArgsConstructor
@Builder
@Jacksonized
public class MatchScoreDto {
    private MatchDto match;
    @Setter
    private long firstPlayerScore;
    @Setter
    private long secondPlayerScore;
}
