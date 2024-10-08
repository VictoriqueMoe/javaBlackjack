package moe.victorique.blackjack.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.victorique.blackjack.annotations.RequestIp;
import moe.victorique.blackjack.dto.ResponseMsg;
import moe.victorique.blackjack.entity.Game;
import moe.victorique.blackjack.service.IUserService;
import moe.victorique.blackjack.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GameController {

    private final IUserService service;

    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponseMsg> getHistory(
            final @NonNull @Parameter(hidden = true) @RequestHeader("User-Agent") String userAgent,
            final @NonNull @Parameter(hidden = true) @RequestIp String ip
    ) {
        final var deviceId = getDeviceId(ip, userAgent);
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
            final @NonNull @Parameter(hidden = true) @RequestIp String ip,
            final @NonNull @Parameter(hidden = true) @RequestHeader("User-Agent") String userAgent
    ) {
        final var deviceId = getDeviceId(ip, userAgent);
        return this.service.getActiveGame(deviceId, token.orElse(null))
                .map(service::hit)
                .map(this::buildFromGame)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active game found"));
    }

    @GetMapping(value = "/deal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsg deal(
            final @NonNull @Parameter(hidden = true) @RequestIp String ip,
            final @NonNull @Parameter(hidden = true) @RequestHeader("User-Agent") String userAgent
    ) {
        final var deviceId = getDeviceId(ip, userAgent);

        log.info("DEAL: {}", deviceId);
        return service.getActiveGame(deviceId, null)
                .or(() -> Optional.of(this.service.newGame(deviceId)))
                .map(this::buildFromGame)
                .get();
    }

    private String getDeviceId(final @NonNull String ip, final @NonNull String userAgent) {
        try {
            return Utils.deviceHash(ip, userAgent);
        } catch (final NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
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
