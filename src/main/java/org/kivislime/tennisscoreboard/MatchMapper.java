package org.kivislime.tennisscoreboard;

import org.kivislime.tennisscoreboard.match.Match;
import org.kivislime.tennisscoreboard.match.MatchDto;
import org.kivislime.tennisscoreboard.match.MatchScore;
import org.kivislime.tennisscoreboard.match.MatchScoreDto;
import org.kivislime.tennisscoreboard.player.Player;
import org.kivislime.tennisscoreboard.player.PlayerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//TODO: разделить мапперы по сущностям
@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "firstPlayer", target = "firstPlayer")
    @Mapping(source = "secondPlayer", target = "secondPlayer")
    @Mapping(source = "winnerPlayer", target = "winnerPlayer")
    MatchDto matchToDto(Match match);

    PlayerDto playerToDto(Player player);

    MatchScoreDto scoreToDto(MatchScore score);
}
