package org.kivislime.tennisscoreboard.mapper;

import org.kivislime.tennisscoreboard.domain.Player;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDto playerToDto(Player player);

    Player playerDtoToPlayer(PlayerDto playerDto);
}
