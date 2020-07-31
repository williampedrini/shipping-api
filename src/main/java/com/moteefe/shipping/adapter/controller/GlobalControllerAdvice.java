package com.moteefe.shipping.adapter.controller;

import com.moteefe.shipping.adapter.controller.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice("com.moteefe.delivery.api.adapter.controller")
class GlobalControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    ErrorResponseDTO handle(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponseDTO(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    List<ErrorResponseDTO> handle(final MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .map(ErrorResponseDTO::new)
                .collect(toUnmodifiableList());
    }
}
