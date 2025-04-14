package org.kivislime.tennisscoreboard;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.kivislime.tennisscoreboard.match.Match;
import org.kivislime.tennisscoreboard.match.MatchRepository;
import org.kivislime.tennisscoreboard.match.MatchRepositoryImpl;
import org.kivislime.tennisscoreboard.match.PaginationConfig;
import org.kivislime.tennisscoreboard.player.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchRepositoryIntegrationTest {

    private MatchRepository matchRepository;

    @BeforeAll
    void setUpAll() {
        matchRepository = new MatchRepositoryImpl();
    }

    @Test
    void getTotalMatches_shouldReturnCorrectValueFromImportSql() {
        long totalMatches = matchRepository.getTotalMatches();
        assertEquals(2, totalMatches);
    }

    @Test
    void getMatches_shouldReturnPaginatedData() {
        List<Match> matches = matchRepository.getMatches(1);
        assertFalse(matches.isEmpty());
        assertTrue(matches.size() <= PaginationConfig.PAGE_SIZE);
    }

    @Test
    void getMatchesByPlayerName_shouldReturnMatchesForKnownPlayer() {
        List<Match> matches = matchRepository.getMatchesByPlayerName("Johan", 1);
        assertFalse(matches.isEmpty());
    }

    @Test
    void addMatch_shouldPersistNewMatch() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Player p1 = session.get(Player.class, 1L);
        Player p2 = session.get(Player.class, 2L);

        Match newMatch = new Match(null, p1, p2, p1);
        Match added = matchRepository.addMatch(newMatch);

        assertNotNull(added.getId());
        session.close();
    }
}
