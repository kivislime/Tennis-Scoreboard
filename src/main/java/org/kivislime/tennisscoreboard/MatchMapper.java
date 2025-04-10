package org.kivislime.tennisscoreboard;

import org.kivislime.tennisscoreboard.match.Match;
import org.kivislime.tennisscoreboard.match.MatchDto;
import org.kivislime.tennisscoreboard.match.MatchScore;
import org.kivislime.tennisscoreboard.match.MatchScoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PlayerScoreMapper.class)
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchDto matchToDto(Match match);

    MatchScoreDto matchScoreToDto(MatchScore score);
}
