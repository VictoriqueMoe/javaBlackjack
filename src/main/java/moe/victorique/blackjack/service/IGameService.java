package moe.victorique.blackjack.service;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import moe.victorique.blackjack.model.StayResponse;
import moe.victorique.blackjack.model.entity.Game;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGameService {
    Game newGame(final @NonNull String deviceId);

    int calculateScore(final @NonNull List<String> cards);

    Game hit(final @NonNull Game game);

    StayResponse stay(final @NonNull Game game);

    Optional<Game> getActiveGame(final @NonNull String deviceId, final @Nullable UUID token);

    List<Game> getAllGames(final @NonNull String deviceId);

    boolean deleteGame(final @Nullable String deviceId, final @Nullable UUID token);
}
