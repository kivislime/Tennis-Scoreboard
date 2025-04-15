package org.kivislime.tennisscoreboard.dto;

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
