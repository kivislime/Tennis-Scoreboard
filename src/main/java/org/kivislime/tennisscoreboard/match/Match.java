package org.kivislime.tennisscoreboard.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kivislime.tennisscoreboard.player.Player;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "player1", nullable = false)
    private Player firstPlayer;
    @ManyToOne
    @JoinColumn(name = "player2", nullable = false)
    private Player secondPlayer;
    @ManyToOne
    @JoinColumn(name = "winner", nullable = false)
    Player winnerPlayer;
}
