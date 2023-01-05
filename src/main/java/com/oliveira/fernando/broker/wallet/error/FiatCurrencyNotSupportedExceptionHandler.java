package com.oliveira.fernando.broker.wallet.error;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import static com.oliveira.fernando.broker.wallet.WalletController.SUPPORTED_FIAT_CURRENCIES;
@Produces
@Requires(classes = {FiatCurrencyNotSupportedException.class, ExceptionHandler.class})
@Singleton
public class FiatCurrencyNotSupportedExceptionHandler implements ExceptionHandler<FiatCurrencyNotSupportedException, HttpResponse<CustomError>> {
    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, FiatCurrencyNotSupportedException exception) {
        return HttpResponse.badRequest(
            new CustomError(
                    HttpResponse.badRequest().code(),
                    "UNSUPPORTED_FIAT_CURRENCIES",
                    String.format("Only %s are supported", SUPPORTED_FIAT_CURRENCIES)
            )
        );
    }
}
