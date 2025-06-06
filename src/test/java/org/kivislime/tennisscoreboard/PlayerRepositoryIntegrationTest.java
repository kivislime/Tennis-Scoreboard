package org.kivislime.tennisscoreboard;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.kivislime.tennisscoreboard.domain.Player;
import org.kivislime.tennisscoreboard.repository.PlayerRepository;
import org.kivislime.tennisscoreboard.repository.PlayerRepositoryImpl;
import org.kivislime.tennisscoreboard.util.HibernateUtil;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerRepositoryIntegrationTest {

    private PlayerRepository playerRepository;

    @BeforeAll
    void setUpAll() {
        playerRepository = new PlayerRepositoryImpl();
    }

    @BeforeEach
    void cleanUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM Match").executeUpdate();
            session.createMutationQuery("DELETE FROM Player").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void addPlayer_shouldPersistNewPlayer() {
        Player player = Player.builder().name("TestPlayer").build();
        Player savedPlayer = playerRepository.persist(player);

        assertNotNull(savedPlayer.getId());
        assertEquals("TestPlayer", savedPlayer.getName());
    }

    @Test
    void findByName() {
        Player player = Player.builder().name("ExistingPlayer").build();
        playerRepository.persist(player);

        var result = playerRepository.findByName("ExistingPlayer");

        assertTrue(result.isPresent());
        assertEquals("ExistingPlayer", result.get().getName());
    }

    @Test
    void findByName_shouldReturnEmptyIfNotFound() {
        var result = playerRepository.findByName("GhostPlayer");

        assertTrue(result.isEmpty());
    }
}
