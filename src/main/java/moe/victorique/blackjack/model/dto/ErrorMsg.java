package moe.victorique.blackjack.model.dto;

import lombok.NonNull;

public record ErrorMsg(int code, @NonNull String message) {
}
