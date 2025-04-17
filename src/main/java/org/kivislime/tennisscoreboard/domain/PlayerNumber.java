package org.kivislime.tennisscoreboard.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum PlayerNumber {
    FIRST(1),
    SECOND(2);

    public static Optional<PlayerNumber> fromInt(int value) {
        switch (value) {
            case 1:
                return Optional.of(FIRST);
            case 2:
                return Optional.of(SECOND);
            default:
                return Optional.empty();
        }
    }

    private final int number;
}
