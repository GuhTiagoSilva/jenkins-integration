package com.demo.jenkinsintegration.dto;

import java.time.Instant;

public record HttpExceptionHandlerResponse(Instant timestamp,
                                           Integer httpStatus,
                                           String error,
                                           String errorMessage,
                                           String path) {
}
