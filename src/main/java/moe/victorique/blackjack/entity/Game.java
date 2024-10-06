package moe.victorique.blackjack.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moe.victorique.blackjack.constants.PlayStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@NoArgsConstructor
public class Game {

    public Game(
            final String deviceId,
            final PlayStatus status
    ) {
        this.device = deviceId;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "TEXT")
    public UUID token;

    @Column(columnDefinition = "TEXT")
    public String device;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = PlayStatusConverter.class)
    public PlayStatus status;

    @Column(columnDefinition = "integer")
    @CreationTimestamp
    public Instant createdOn;

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    public List<String> deck = new ArrayList<>();

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    public List<String> dealerCards = new ArrayList<>();

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    public List<String> playerCards = new ArrayList<>();

}