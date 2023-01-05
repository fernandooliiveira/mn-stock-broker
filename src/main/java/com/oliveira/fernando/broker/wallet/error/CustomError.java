package com.oliveira.fernando.broker.wallet.error;

import com.oliveira.fernando.broker.api.RestApiResponse;

public record CustomError(
    int status,
    String error,
    String message
) implements RestApiResponse {
}
