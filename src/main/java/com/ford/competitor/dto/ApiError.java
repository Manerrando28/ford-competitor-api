package com.ford.competitor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Resposta de erro padronizada da API")
public class ApiError {

    @Schema(example = "2026-09-27T15:30:00Z")
    private final Instant timestamp;
    @Schema(example = "400")
    private final int status;
    @Schema(example = "Bad Request")
    private final String error;
    @Schema(example = "validation_error")
    private final String code;
    @Schema(example = "Há campos inválidos na requisição")
    private final String message;
    @Schema(example = "/api/auth/login")
    private final String path;
    private final Map<String, String> fieldErrors;

    public ApiError(HttpStatus status, String code, String message, String path) {
        this(status, code, message, path, Map.of());
    }

    public ApiError(HttpStatus status, String code, String message, String path,
                    Map<String, String> fieldErrors) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.code = code;
        this.message = message;
        this.path = path;
        this.fieldErrors = new LinkedHashMap<>(fieldErrors);
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

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
