package org.kivislime.tennisscoreboard;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MatchRepositoryImplTest {

    private static SessionFactory sessionFactory;
    private MatchRepositoryImpl matchRepository;

    @BeforeAll
    public static void setUpAll() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @AfterAll
    public static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    public void setUp() {
        matchRepository = new MatchRepositoryImpl();
    }

    @Test
    public void testGetMatches() {
        List<Match> matches = matchRepository.getMatches();
        assertNotNull(matches, "Список матчей не должен быть null");
        assertFalse(matches.isEmpty(), "Ожидается, что в базе будет хотя бы один матч");
    }

    @Test
    public void testGetMatchesByPlayerNameFound() {
        List<Match> matches = matchRepository.getMatchesByPlayerName("Kohan");
        assertNotNull(matches, "Список матчей не должен быть null");
        assertFalse(matches.isEmpty(), "Ожидается, что найдутся матчи для игрока 'Kohan'");
    }

    //TODO: with new exception
//    @Test
//    public void testGetMatchesByPlayerNameNotFound() {
//        HibernateException exception = assertThrows(HibernateException.class, () -> {
//            matchRepository.getMatchesByPlayerName("NonExistingPlayer");
//        });
//        assertTrue(exception.getMessage().contains("Match not found"),
//                "Ожидается сообщение об ошибке 'Match not found'");
//    }
}
