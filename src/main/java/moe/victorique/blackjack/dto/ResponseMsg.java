package moe.victorique.blackjack.dto;

import lombok.NonNull;
import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.entity.Game;

import java.util.List;
import java.util.UUID;

public record ResponseMsg(
        @NonNull UUID token,
        @NonNull String device,
        @NonNull List<String> cards,
        @NonNull List<String> dealerCards,
        int handValue,
        int dealerValue,
        @NonNull PlayStatus playStatus
) {
    public static ResponseMsg fromGame(
            final @NonNull Game game,
            final int handValue,
            final int dealerValue,
            final @NonNull List<String> deck
    ) {
        return new ResponseMsg(
                game.token,
                game.device,
                game.playerCards,
                deck,
                handValue,
                dealerValue,
                game.status
        );
    }
}

