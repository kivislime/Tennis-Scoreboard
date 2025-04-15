package org.kivislime.tennisscoreboard.mapper;

import org.kivislime.tennisscoreboard.domain.PlayerScore;
import org.kivislime.tennisscoreboard.dto.PlayerScoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerScoreMapper {
    PlayerScoreMapper INSTANCE = Mappers.getMapper(PlayerScoreMapper.class);

    @Mapping(source = "points.value", target = "points")
    PlayerScoreDto playerScoreToDto(PlayerScore playerScore);
}