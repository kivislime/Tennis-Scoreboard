package org.kivislime.tennisscoreboard;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "firstPlayer", target = "firstPlayer")
    @Mapping(source = "secondPlayer", target = "secondPlayer")
    @Mapping(source = "winnerPlayer", target = "winnerPlayer")
    MatchDto matchToDto(Match match);

    PlayerDto playerToDto(Player player);
}
