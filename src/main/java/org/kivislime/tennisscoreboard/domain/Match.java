package org.kivislime.tennisscoreboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
