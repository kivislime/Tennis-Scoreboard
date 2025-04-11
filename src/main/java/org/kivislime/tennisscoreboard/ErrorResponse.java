package org.kivislime.tennisscoreboard;

import lombok.Value;

import java.time.Instant;

@Value
public class ErrorResponse {
    String errorCode;
    String message;
    Instant timestamp;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

}
