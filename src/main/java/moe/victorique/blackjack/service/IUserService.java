package moe.victorique.blackjack.service;

import moe.victorique.blackjack.entity.Game;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    Game newGame(final String deviceId);

    int calculateScore(final List<String> cards);

    Game hit(final Game game);

    Pair<Integer, Integer> stay(final Game game);

    Optional<Game> getActiveGame(final String deviceId, final Optional<UUID> token);

    Optional<Game> getGameFromToken(final UUID token);

    List<Game> getAllGames(final String deviceId);
}
