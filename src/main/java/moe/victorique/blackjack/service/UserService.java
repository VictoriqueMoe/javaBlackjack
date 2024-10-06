package moe.victorique.blackjack.service;

import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.entity.Game;
import moe.victorique.blackjack.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService implements IUserService {

    private final UserRepository repo;

    private final List<Character> suites = List.of(
            '♠',
            '♣',
            '♥',
            '♦'
    );

    private final List<String> faces = List.of(
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "A",
            "J",
            "Q",
            "K"
    );

    @Autowired
    public UserService(final UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public Game newGame(String deviceId) {
        var newGame = new Game(deviceId, PlayStatus.Playing);
        this.createDeck(newGame);
        this.deal(newGame);
        return this.repo.save(newGame);
    }

    @Override
    public int calculateScore(List<String> cards) {
        return 0;
    }

    @Override
    public void hit(Game game) {

    }

    @Override
    public Pair<Integer, Integer> stay(Game game) {
        return null;
    }

    @Override
    public Optional<Game> getActiveGame(final String deviceId) {
        return this.repo.findByDeviceAndStatus(deviceId, PlayStatus.Playing);
    }

    @Override
    public Optional<Game> getGameFromToken(UUID token) {
        return Optional.empty();
    }

    @Override
    public List<Game> getAllGames(String deviceId) {
        return List.of();
    }

    private void createDeck(Game game) {
        for (final var suite : this.suites) {
            for (final var face : this.faces) {
                game.deck.add(suite + face);
            }
        }
        for (var i = 0; i < game.deck.size(); i++) {
            var j = (int) (Math.random() * game.deck.size());
            var originalCard = game.deck.get(i);
            game.deck.set(i, game.deck.get(j));
            game.deck.set(j, originalCard);
        }
    }

    private void deal(Game game) {
        game.playerCards.add(game.deck.removeLast());
        game.dealerCards.add(game.deck.removeLast());
        game.playerCards.add(game.deck.removeLast());
        game.dealerCards.add(game.deck.removeLast());
    }

}