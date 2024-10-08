package moe.victorique.blackjack.service;

import lombok.NonNull;
import moe.victorique.blackjack.constants.Action;
import moe.victorique.blackjack.entity.Stat;

import java.util.Optional;

public interface IStatService {
    Stat updateStats(final @NonNull String deviceId, final @NonNull Action action);

    Optional<Stat> getAllStat(final @NonNull String deviceId);
}
