package org.kivislime.tennisscoreboard.domain;

import lombok.Getter;

@Getter
public class PlayerScore {
    private Integer sets;
    private Integer games;
    private Point points;

    private int tieBreakPoints;

    public PlayerScore() {
        this.sets = 0;
        this.games = 0;
        this.points = Point.ZERO;
    }

    public void winPoint() {
        points = points.next();
    }

    public void winTieBreakPoint() {
        tieBreakPoints++;
    }

    public void winGame() {
        ++games;
        points = Point.ZERO;
        tieBreakPoints = 0;
    }

    public void winSet() {
        ++sets;
        games = 0;
        points = Point.ZERO;
        tieBreakPoints = 0;
    }

    public void loseGame() {
        points = Point.ZERO;
    }

    public void loseSet() {
        games = 0;
        points = Point.ZERO;
        tieBreakPoints = 0;
    }

    public void setAdvantage() {
        points = Point.ADVANTAGE;
    }

    public void loseAdvantage() {
        if (points == Point.ADVANTAGE) {
            points = Point.FORTY;
        }
    }

    public boolean hasAdvantage() {
        return points == Point.ADVANTAGE;
    }

    //Using in PlayerScoreMapper
    public int getDisplayPoints() {
        boolean isTieBreak = this.tieBreakPoints > 0;
        return isTieBreak ? this.tieBreakPoints : this.points.getValue();
    }
}
