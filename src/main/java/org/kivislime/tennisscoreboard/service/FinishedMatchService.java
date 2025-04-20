package org.kivislime.tennisscoreboard.service;

import org.kivislime.tennisscoreboard.dto.MatchDto;

public interface FinishedMatchService {
    MatchDto persistFinishedMatch(MatchDto build);
}
