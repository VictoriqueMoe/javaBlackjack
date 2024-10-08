package moe.victorique.blackjack.model.dto;

import lombok.NonNull;
import moe.victorique.blackjack.model.entity.Stat;

public record StatusMsg(int wins, int loses, int draws) {

    public static StatusMsg fromStat(final @NonNull Stat status) {
        return new StatusMsg(status.wins, status.loses, status.draws);
    }
}
