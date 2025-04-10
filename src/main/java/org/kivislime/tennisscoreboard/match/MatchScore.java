package org.kivislime.tennisscoreboard.match;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class MatchScore {
    public static final int GAMES_DIFFERENCE_TO_WIN_SET = 2;
    public static final int GAMES_BEFORE_TIE_BREAK = 6;
    public static final int MAX_SETS_FOR_WIN = 2;

    private final PlayerScore firstPlayerScore;
    private final PlayerScore secondPlayerScore;
    private final MatchDto matchDto;

    public void processPointWinner(PlayerNumber numWinnerPoint) {

        if (numWinnerPoint == PlayerNumber.FIRST) {
            processPoints(firstPlayerScore, secondPlayerScore);
        } else {
            processPoints(secondPlayerScore, firstPlayerScore);
        }

        //TODO: constant - max games?
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
}
