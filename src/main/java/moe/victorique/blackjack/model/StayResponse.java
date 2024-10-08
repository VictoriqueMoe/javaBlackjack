package moe.victorique.blackjack.model;

import lombok.NonNull;
import moe.victorique.blackjack.model.entity.Game;

public record StayResponse(@NonNull Game game, int playerScore, int dealerScore) {
}
