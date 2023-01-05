package com.oliveira.fernando.broker.wallet;

import com.oliveira.fernando.broker.api.RestApiResponse;
import com.oliveira.fernando.broker.symbols.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked
) implements RestApiResponse {
    public Wallet addAvailable(BigDecimal amountToAdd) {
        return new Wallet(
            this.accountId,
            this.walletId,
            this.symbol,
            this.available.add(amountToAdd),
            this.locked
        );
    }

    public Wallet withdrawAvailable(BigDecimal amountToRemove) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.subtract(amountToRemove),
                this.locked
        );
    }
}
