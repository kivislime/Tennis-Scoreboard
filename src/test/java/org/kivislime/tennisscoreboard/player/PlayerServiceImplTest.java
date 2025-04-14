package org.kivislime.tennisscoreboard.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        when(playerRepository.getPlayer("Rafael"))
                .thenReturn(Optional.of(existingPlayer));

        Optional<PlayerDto> result = playerService.getPlayer("Rafael");

        assertTrue(result.isPresent());
        assertEquals("Rafael", result.get().getName());
        verify(playerRepository).getPlayer("Rafael");
    }

    @Test
    void testGetPlayer_WhenNotExists_ShouldReturnEmptyOptional() {
        when(playerRepository.getPlayer("Novak"))
                .thenReturn(Optional.empty());

        Optional<PlayerDto> result = playerService.getPlayer("Novak");

        assertFalse(result.isPresent());
        verify(playerRepository).getPlayer("Novak");
    }
}
