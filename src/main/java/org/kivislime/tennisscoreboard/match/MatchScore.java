package org.kivislime.tennisscoreboard.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchScore {
    private MatchDto match;
    private long firstPlayerScore;
    private long secondPlayerScore;

    public void incrementFirstPlayerScore() {
        ++firstPlayerScore;
    }

    public void incrementSecondPlayerScore() {
        ++secondPlayerScore;
    }
}
