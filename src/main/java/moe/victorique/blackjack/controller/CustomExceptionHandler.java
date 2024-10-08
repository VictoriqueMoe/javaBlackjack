package moe.victorique.blackjack.controller;

import lombok.NonNull;
import moe.victorique.blackjack.dto.ErrorMsg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMsg> handleResponseException(final @NonNull ResponseStatusException ex) {
        return new ResponseEntity<>(new ErrorMsg(ex.getStatusCode().value(), ex.getMessage()), ex.getStatusCode());
    }
}
