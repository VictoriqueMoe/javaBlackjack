package moe.victorique.blackjack.service.impl;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.victorique.blackjack.constants.Action;
import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.model.StayResponse;
import moe.victorique.blackjack.model.entity.Game;
import moe.victorique.blackjack.repo.GameRepository;
import moe.victorique.blackjack.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GameService implements IUserService {

    private final GameRepository repo;

    private final StatService statService;

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

    @Override
    public Game newGame(final @NonNull String deviceId) {
        final var newGame = new Game(deviceId, PlayStatus.Playing);
        this.createDeck(newGame);
        this.deal(newGame);
        return this.repo.save(newGame);
    }

    @Override
    public int calculateScore(final @NonNull List<String> cards) {
        var retVal = 0;
        var hasAce = false;
        for (final var card : cards) {
            retVal += getNumberFromCard(card);
            if (card.contains("J") || card.contains("Q") || card.contains("K")) {
                retVal += 10;
                continue;
            }
            if (card.contains("A")) {
                hasAce = true;
            }
        }
        if (hasAce) {
            for (final var card : cards) {
                if (card.contains("A")) {
                    if (retVal + 11 > 21) {
                        retVal += 1;
                    } else {
                        retVal += 11;
                    }
                }
            }
        }
        return retVal;
    }

    @Override
    public Game hit(final @NonNull Game game) {
        game.playerCards.add(game.deck.removeLast());
        log.info("HIT: {}", game.token);

        if (this.calculateScore(game.playerCards) > 21) {
            game.status = PlayStatus.Bust;
            log.info("BUST");
        }

        if (game.status == PlayStatus.Bust) {
            return this.doBust(game);
        }

        return this.repo.save(game);
    }

    @Override
    public StayResponse stay(final @NonNull Game game) {
        while (this.calculateScore(game.dealerCards) < 17) {
            game.dealerCards.add(game.deck.removeLast());
        }

        final var playerScore = this.calculateScore(game.playerCards);
        final var dealerScore = this.calculateScore(game.dealerCards);

        if (dealerScore > 21) {
            game.status = PlayStatus.DealerBust;
            log.info("DEALER BUST");
            this.statService.updateStats(game.device, Action.WIN);
        } else if (playerScore > dealerScore) {
            game.status = PlayStatus.PlayerWins;
            log.info("WIN");
            this.statService.updateStats(game.device, Action.WIN);
        } else if (dealerScore > playerScore) {
            game.status = PlayStatus.DealerWins;
            log.info("LOSE");
            this.statService.updateStats(game.device, Action.LOSE);
        } else {
            game.status = PlayStatus.Draw;
            log.info("DRAW");
            this.statService.updateStats(game.device, Action.DRAW);
        }

        return new StayResponse(this.repo.save(game), playerScore, dealerScore);
    }

    @Override
    public Optional<Game> getActiveGame(final @NonNull String deviceId, final @Nullable UUID token) {
        final var status = PlayStatus.Playing;
        return token == null ? this.repo.findByDeviceAndStatus(deviceId, status) : this.repo.findByTokenAndStatus(token, status);
    }

    @Override
    public List<Game> getAllGames(final @NonNull String deviceId) {
        return this.repo.findAllByDeviceAndStatusNot(deviceId, PlayStatus.Playing);
    }

    private void createDeck(final Game game) {
        for (final var suite : this.suites) {
            for (final var face : this.faces) {
                game.deck.add(suite + face);
            }
        }
        Collections.shuffle(game.deck, new Random());
    }

    private void deal(final @NonNull Game game) {
        game.playerCards.add(game.deck.removeLast());
        game.dealerCards.add(game.deck.removeLast());
        game.playerCards.add(game.deck.removeLast());
        game.dealerCards.add(game.deck.removeLast());
    }

    private int getNumberFromCard(final @NonNull String card) {
        try {
            return Integer.parseInt(card.replaceAll("[^0-9]", ""));
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

    private Game doBust(final @NonNull Game game) {
        statService.updateStats(game.device, Action.LOSE);
        return repo.save(game);
    }

}
