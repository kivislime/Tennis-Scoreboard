package org.kivislime.tennisscoreboard.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static org.kivislime.tennisscoreboard.config.MatchConstants.*;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public final class MatchScore {
    private final PlayerScore firstPlayerScore;
    private final PlayerScore secondPlayerScore;
    private final Match match;

    public void processPointWinner(PlayerNumber numWinnerPoint) {
        if (isTieBreak()) {
            processTieBreakPoints(numWinnerPoint);
            return;
        }

        processPoints(numWinnerPoint);

        if (isSetWon(firstPlayerScore, secondPlayerScore)) {
            firstPlayerScore.winSet();
            secondPlayerScore.loseSet();
        } else if (isSetWon(secondPlayerScore, firstPlayerScore)) {
            secondPlayerScore.winSet();
            firstPlayerScore.loseSet();
        }
    }

    public boolean isTieBreak() {
        return firstPlayerScore.getGames() == GAMES_BEFORE_TIE_BREAK && secondPlayerScore.getGames() == GAMES_BEFORE_TIE_BREAK;
    }

    public boolean isMaxGames() {
        return firstPlayerScore.getGames() > MAX_GAMES_IN_SET && secondPlayerScore.getGames() > MAX_GAMES_IN_SET;
    }

    private void processTieBreakPoints(PlayerNumber numWinnerPoint) {
        PlayerScore winner = PlayerNumber.FIRST == numWinnerPoint ? firstPlayerScore : secondPlayerScore;
        PlayerScore loser = PlayerNumber.FIRST == numWinnerPoint ? secondPlayerScore : firstPlayerScore;

        winner.winTieBreakPoint();

        if (winner.getTieBreakPoints() >= TIE_BREAK_WIN_POINTS &&
                winner.getTieBreakPoints() - loser.getTieBreakPoints() >= POINTS_DIFFERENCE_TO_WIN_GAME) {
            winner.winSet();
            loser.loseSet();
        }
    }

    private void processPoints(PlayerNumber numWinnerPoint) {
        PlayerScore winner = PlayerNumber.FIRST == numWinnerPoint ? firstPlayerScore : secondPlayerScore;
        PlayerScore loser = PlayerNumber.FIRST == numWinnerPoint ? secondPlayerScore : firstPlayerScore;

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
}
