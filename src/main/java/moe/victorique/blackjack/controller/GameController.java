package moe.victorique.blackjack.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.victorique.blackjack.annotations.DeviceId;
import moe.victorique.blackjack.model.dto.ResponseMsg;
import moe.victorique.blackjack.model.dto.StatusMsg;
import moe.victorique.blackjack.model.entity.Game;
import moe.victorique.blackjack.service.IStatService;
import moe.victorique.blackjack.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GameController {

    private final IUserService service;

    private final IStatService statService;

    @GetMapping(value = "/stay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsg stay(
            final @NonNull @RequestParam Optional<UUID> token,
            final @NonNull @Parameter(hidden = true) @DeviceId String deviceId
    ) {
        return this.service
                .getActiveGame(deviceId, token.orElse(null))
                .map(service::stay)
                .map(stayResponse -> this.buildFromGame(stayResponse.game(), stayResponse.playerScore(), stayResponse.dealerScore()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No game is in progress"));
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusMsg getStats(final @NonNull @Parameter(hidden = true) @DeviceId String deviceId) {
        return this.statService
                .getAllStat(deviceId)
                .map(StatusMsg::fromStat)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No stats found for device"));
    }

    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponseMsg> getHistory(final @NonNull @Parameter(hidden = true) @DeviceId String deviceId) {
        return this.service
                .getAllGames(deviceId)
                .stream()
                .map(game -> this.buildFromGame(
                                game,
                                this.service.calculateScore(game.playerCards),
                                this.service.calculateScore(game.dealerCards)
                        )
                )
                .toList();
    }


    @GetMapping(value = "/hit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsg hit(
            final @NonNull @RequestParam Optional<UUID> token,
            final @NonNull @Parameter(hidden = true) @DeviceId String deviceId
    ) {
        return this.service.getActiveGame(deviceId, token.orElse(null))
                .map(service::hit)
                .map(this::buildFromGame)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active game found"));
    }

    @GetMapping(value = "/deal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsg deal(final @NonNull @Parameter(hidden = true) @DeviceId String deviceId) {
        log.info("DEAL: {}", deviceId);
        return service.getActiveGame(deviceId, null)
                .or(() -> Optional.of(this.service.newGame(deviceId)))
                .map(this::buildFromGame)
                .get();
    }

    private ResponseMsg buildFromGame(final @NonNull Game game) {
        return ResponseMsg.fromGame(game, this.service.calculateScore(game.playerCards), 0, List.of());
    }

    private ResponseMsg buildFromGame(
            final @NonNull Game game,
            final int handValue,
            final int dealerValue
    ) {
        return ResponseMsg.fromGame(game, handValue, dealerValue, game.deck);
    }
}
