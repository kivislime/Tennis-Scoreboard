package org.kivislime.tennisscoreboard;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PlayerDto {
    Long id;
    String name;
}
