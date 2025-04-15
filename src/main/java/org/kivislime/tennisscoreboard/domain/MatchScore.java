package org.kivislime.tennisscoreboard.domain;

import lombok.*;
import org.kivislime.tennisscoreboard.dto.MatchDto;

import static org.kivislime.tennisscoreboard.config.MatchConstants.GAMES_BEFORE_TIE_BREAK;
import static org.kivislime.tennisscoreboard.config.MatchConstants.GAMES_DIFFERENCE_TO_WIN_SET;
import static org.kivislime.tennisscoreboard.config.MatchConstants.MAX_GAMES_IN_SET;

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

    private void processPoints(PlayerScore playerWinner, PlayerScore playerLoser) {
        if (playerLoser.hasAdvantage()) {
            playerLoser.loseAdvantage();
            return;
        }

        if (playerWinner.getPoints() == Point.FORTY) {
            if (playerLoser.getPoints() == Point.FORTY) {
                playerWinner.setAdvantage();
            } else {
                playerWinner.winGame();
                playerLoser.loseGame();
            }
        } else if (playerWinner.hasAdvantage()) {
            playerWinner.winGame();
            playerLoser.loseGame();
        } else {
            playerWinner.winPoint();
        }

    }

    private boolean isSetWon(PlayerScore player, PlayerScore opponent) {
        return player.getGames() >= GAMES_BEFORE_TIE_BREAK &&
                player.getGames() - opponent.getGames() >= GAMES_DIFFERENCE_TO_WIN_SET;
    }

    public boolean isMaxGames() {
        return firstPlayerScore.getGames() > MAX_GAMES_IN_SET && secondPlayerScore.getGames() > MAX_GAMES_IN_SET;
    }
}
