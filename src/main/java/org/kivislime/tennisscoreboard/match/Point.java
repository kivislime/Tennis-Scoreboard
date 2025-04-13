package org.kivislime.tennisscoreboard.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Point {
    ZERO(0),
    FIFTEEN(15),
    THIRTY(30),
    FORTY(40),
    ADVANTAGE(-1);

    private final int value;

    public Point next() {
        switch (this) {
            case ZERO:
                return FIFTEEN;
            case FIFTEEN:
                return THIRTY;
            case THIRTY:
            case FORTY:
                return FORTY;
            default:
                throw new IllegalArgumentException("Unknown state: " + this);
        }
    }
}