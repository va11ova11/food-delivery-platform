package com.mycompany.fooddeliveryplatform.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String path;
    private Map<String, String> fieldErrors; // можно null

    public ErrorResponse(Instant timestamp, int status, String error, String path) {
        this(timestamp, status, error, path, null);
    }

    public ErrorResponse(Instant timestamp, int status, String error, String path,
                         Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
