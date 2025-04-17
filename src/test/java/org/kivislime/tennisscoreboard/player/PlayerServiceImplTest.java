package org.kivislime.tennisscoreboard.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kivislime.tennisscoreboard.domain.Player;
import org.kivislime.tennisscoreboard.dto.PlayerDto;
import org.kivislime.tennisscoreboard.repository.PlayerRepository;
import org.kivislime.tennisscoreboard.service.PlayerServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    private PlayerRepository playerRepository;
    private PlayerServiceImpl playerService;

    @BeforeEach
    void setUp() {
        playerRepository = mock(PlayerRepository.class);
        playerService = new PlayerServiceImpl(playerRepository);
    }

    @Test
    void testAddPlayer_ShouldReturnMappedDto() {
        Player savedPlayer = Player.builder()
                .name("Roger")
                .build();

        when(playerRepository.addPlayer(any(Player.class)))
                .thenReturn(savedPlayer);

        PlayerDto result = playerService.addPlayer("Roger");

        assertNotNull(result);
        assertEquals("Roger", result.getName());
        verify(playerRepository).addPlayer(any(Player.class));
    }

    @Test
    void testGetPlayer_WhenExists_ShouldReturnDto() {
        Player existingPlayer = Player.builder()
                .name("Rafael")
                .build();

        when(playerRepository.findByName("Rafael"))
                .thenReturn(Optional.of(existingPlayer));

        Optional<PlayerDto> result = playerService.getPlayer("Rafael");

        assertTrue(result.isPresent());
        assertEquals("Rafael", result.get().getName());
        verify(playerRepository).findByName("Rafael");
    }

    @Test
    void testGetPlayer_WhenNotExists_ShouldReturnEmptyOptional() {
        when(playerRepository.findByName("Novak"))
                .thenReturn(Optional.empty());

        Optional<PlayerDto> result = playerService.getPlayer("Novak");

        assertFalse(result.isPresent());
        verify(playerRepository).findByName("Novak");
    }
}
