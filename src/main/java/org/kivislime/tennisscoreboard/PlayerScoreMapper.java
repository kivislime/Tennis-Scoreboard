package org.kivislime.tennisscoreboard;

import org.kivislime.tennisscoreboard.match.PlayerScore;
import org.kivislime.tennisscoreboard.match.PlayerScoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerScoreMapper {
    PlayerScoreMapper INSTANCE = Mappers.getMapper(PlayerScoreMapper.class);

    @Mapping(source = "points.value", target = "points")
    PlayerScoreDto playerScoreToDto(PlayerScore playerScore);
}