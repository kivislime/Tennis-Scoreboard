package org.kivislime.tennisscoreboard.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerNumber {
    FIRST(1),
    SECOND(2);

    private final int number;
}
