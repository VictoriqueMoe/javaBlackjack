package moe.victorique.blackjack.service;

import moe.victorique.blackjack.constants.Action;
import moe.victorique.blackjack.entity.Stat;

import java.util.Optional;

public interface IStatService {
    Stat updateStats(final String deviceId, final Action action);

    Optional<Stat> getAllStat(final String deviceId);
}
