package moe.victorique.blackjack.dto;

import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.entity.Game;

import java.util.List;
import java.util.UUID;

public record ResponseMsg(
        UUID token,
        String device,
        List<String> cards,
        List<String> dealerCards,
        int handValue,
        int dealerValue,
        PlayStatus playStatus
) {
    public static ResponseMsg fromGame(
            final Game game,
            final int handValue,
            final int dealerValue,
            final List<String> deck
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

