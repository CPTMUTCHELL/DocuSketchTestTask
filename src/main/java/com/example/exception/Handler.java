package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class Handler {
    @ExceptionHandler({CustomException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("Exception is handled - Response {}, debugMessage: {} ",
                ex.getResponseStatus().value(),
                ex.getMessage());

        return new ResponseEntity<>(ex.getErrorResponse(), ex.getResponseStatus());
    }

}
