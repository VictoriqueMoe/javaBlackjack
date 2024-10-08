package moe.victorique.blackjack.dto;

import lombok.NonNull;

public record ErrorMsg(int code, @NonNull String message) {
}
