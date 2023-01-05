package com.oliveira.fernando.broker.wallet;

import com.oliveira.fernando.broker.symbols.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositFiatMoney(UUID accountId, UUID walletId, Symbol symbol, BigDecimal amount) {
}
