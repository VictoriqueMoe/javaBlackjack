package moe.victorique.blackjack.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
            final @NonNull String deviceId,
            final @NonNull PlayStatus status
    ) {
        this.device = deviceId;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "TEXT")
    @NonNull
    public UUID token;

    @Column(columnDefinition = "TEXT")
    @NonNull
    public String device;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = PlayStatusConverter.class)
    @NonNull
    public PlayStatus status;

    @Column(columnDefinition = "integer")
    @CreationTimestamp
    @NonNull
    public Instant createdOn;

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    @NonNull
    public List<String> deck = new ArrayList<>();

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    @NonNull
    public List<String> dealerCards = new ArrayList<>();

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    @NonNull
    public List<String> playerCards = new ArrayList<>();

}
