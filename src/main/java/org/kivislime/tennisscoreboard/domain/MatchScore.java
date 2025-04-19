package org.kivislime.tennisscoreboard.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.kivislime.tennisscoreboard.dto.MatchDto;

import static org.kivislime.tennisscoreboard.config.MatchConstants.*;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public final class MatchScore {
    private final PlayerScore firstPlayerScore;
    private final PlayerScore secondPlayerScore;
    private final MatchDto matchDto;

    public void processPointWinner(PlayerNumber numWinnerPoint) {

        if (numWinnerPoint == PlayerNumber.FIRST) {
            processPoints(firstPlayerScore, secondPlayerScore);
        } else {
            processPoints(secondPlayerScore, firstPlayerScore);
        }

        if (isSetWon(firstPlayerScore, secondPlayerScore)) {
            firstPlayerScore.winSet();
            secondPlayerScore.loseSet();
        } else if (isSetWon(secondPlayerScore, firstPlayerScore)) {
            secondPlayerScore.winSet();
            firstPlayerScore.loseSet();
        }
    }

    private void processPoints(PlayerScore winner, PlayerScore loser) {
        if (winner.hasAdvantage()) {
            winner.winGame();
            loser.loseGame();
            return;
        }

        if (loser.hasAdvantage()) {
            loser.loseAdvantage();
            return;
        }

        if (winner.getPoints() == Point.FORTY && loser.getPoints() == Point.FORTY) {
            winner.setAdvantage();
            return;
        }

        if (winner.getPoints() == Point.FORTY) {
            winner.winGame();
            loser.loseGame();
            return;
        }

        winner.winPoint();
    }

    private boolean isSetWon(PlayerScore player, PlayerScore opponent) {
        return player.getGames() >= GAMES_BEFORE_TIE_BREAK &&
                player.getGames() - opponent.getGames() >= GAMES_DIFFERENCE_TO_WIN_SET;
    }

    public boolean isMaxGames() {
        return firstPlayerScore.getGames() > MAX_GAMES_IN_SET && secondPlayerScore.getGames() > MAX_GAMES_IN_SET;
    }
}
