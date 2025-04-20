package org.kivislime.tennisscoreboard.mapper;

import org.kivislime.tennisscoreboard.domain.Match;
import org.kivislime.tennisscoreboard.domain.MatchScore;
import org.kivislime.tennisscoreboard.dto.MatchDto;
import org.kivislime.tennisscoreboard.dto.MatchScoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PlayerScoreMapper.class)
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchDto matchToDto(Match match);

    MatchScoreDto matchScoreToDto(MatchScore score);

    Match dtoToMatch(MatchDto match);
}
