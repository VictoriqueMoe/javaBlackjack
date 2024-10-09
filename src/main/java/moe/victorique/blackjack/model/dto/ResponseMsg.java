package moe.victorique.blackjack.model.dto;

import lombok.NonNull;
import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.model.entity.Game;

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
            final int dealerValue
    ) {
        return new ResponseMsg(
                game.token,
                game.device,
                game.playerCards,
                game.dealerCards,
                handValue,
                dealerValue,
                game.status
        );
    }
}

