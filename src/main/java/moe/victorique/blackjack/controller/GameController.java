package moe.victorique.blackjack.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import moe.victorique.blackjack.dto.ResponseMsg;
import moe.victorique.blackjack.entity.Game;
import moe.victorique.blackjack.service.IUserService;
import moe.victorique.blackjack.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final IUserService service;

    @GetMapping(value = "/hit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMsg> hit(
            final @RequestParam Optional<UUID> token,
            final HttpServletRequest request
    ) {
        final var deviceId = getDeviceId(request);
        return this.service.getActiveGame(deviceId, token)
                .map(service::hit)
                .map(this::buildFromGame)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active game found"));
    }

    @GetMapping(value = "/deal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMsg> deal(final HttpServletRequest request) {
        final var deviceId = getDeviceId(request);

        this.logger.info("DEAL: {}", deviceId);

        return service.getActiveGame(deviceId, Optional.empty())
                .or(() -> Optional.of(this.service.newGame(deviceId)))
                .map(this::buildFromGame)
                .map(ResponseEntity::ok)
                .get();
    }

    private String getDeviceId(final HttpServletRequest request) {
        final var ip = request.getRemoteAddr();
        final var userAgent = request.getHeader("User-Agent");
        try {
            return Utils.deviceHash(ip, userAgent);
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    private ResponseMsg buildFromGame(final Game game) {
        return ResponseMsg.fromGame(game, this.service.calculateScore(game.playerCards), 0, List.of());
    }

    private ResponseMsg buildFromGame(
            final Game game,
            final int handValue,
            final int dealerValue
    ) {
        return ResponseMsg.fromGame(game, handValue, dealerValue, game.deck);
    }
}
