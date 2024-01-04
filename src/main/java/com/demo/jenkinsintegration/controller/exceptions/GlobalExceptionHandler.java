package com.demo.jenkinsintegration.controller.exceptions;

import com.demo.jenkinsintegration.dto.HttpExceptionHandlerResponse;
import com.demo.jenkinsintegration.exception.BookNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<HttpExceptionHandlerResponse> bookNotFoundException(BookNotFoundException e, HttpServletRequest req) {

        log.error("Book not found", e);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        HttpExceptionHandlerResponse httpExceptionHandlerResponse = new HttpExceptionHandlerResponse(
                Instant.now(),
                httpStatus.value(),
                "Book not found",
                e.getMessage(),
                req.getRequestURI()
        );

        return new ResponseEntity<>(httpExceptionHandlerResponse, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpExceptionHandlerResponse> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {

        log.error("Validation errors", e);

        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        HttpExceptionHandlerResponse httpExceptionHandlerResponse = new HttpExceptionHandlerResponse(
                Instant.now(),
                httpStatus.value(),
                "Validation error",
                e.getMessage(),
                req.getRequestURI()
        );

        return new ResponseEntity<>(httpExceptionHandlerResponse, httpStatus);
    }

}
