package moe.victorique.blackjack.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import moe.victorique.blackjack.constants.Action;
import moe.victorique.blackjack.entity.Stat;
import moe.victorique.blackjack.repo.StatRepository;
import moe.victorique.blackjack.service.IStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatService implements IStatService {

    private final StatRepository repo;

    @Override
    public Stat updateStats(final @NonNull String deviceId, final @NonNull Action action) {
        return repo
                .findById(deviceId)
                .or(() -> Optional.of(new Stat(deviceId, 0, 0, 0)))
                .map(stat -> {
                    switch (action) {
                        case WIN -> stat.wins++;
                        case LOSE -> stat.loses++;
                        case DRAW -> stat.draws++;
                    }
                    return repo.save(stat);
                })
                .get();
    }

    @Override
    public Optional<Stat> getAllStat(final @NonNull String deviceId) {
        return repo.findById(deviceId);
    }
}
