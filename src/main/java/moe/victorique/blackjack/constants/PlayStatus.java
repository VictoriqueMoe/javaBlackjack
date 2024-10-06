package moe.victorique.blackjack.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PlayStatus {
    DealerBust("Dealer Bust"),
    PlayerWins("Player Wins"),
    DealerWins("Dealer Wins"),
    Draw("Draw"),
    Playing("Playing"),
    Bust("Bust");

    private final String status;

    PlayStatus(final String status) {
        this.status = status;
    }
}
