package com.oliveira.fernando.broker.data;

import com.oliveira.fernando.broker.wallet.DepositFiatMoney;
import com.oliveira.fernando.broker.wallet.Wallet;
import com.oliveira.fernando.broker.wallet.WithdrawFiatMoney;
import com.oliveira.fernando.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class InMemoryAccountStore {

    public static final UUID ACCOUNT_ID = UUID.fromString("7fd03dd4-8c68-11ed-a1eb-0242ac120002");
    public final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<>();
    public final HashMap<UUID, Map<UUID, Wallet>> walletPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID accountId) {
        return watchListPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchListBy(final UUID accountId) {
        watchListPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletPerAccount.get(accountId))
                .orElse(new HashMap<>())
                .values();
    }

    public Wallet depositToWallet(DepositFiatMoney deposit) {
        final var wallets = Optional.ofNullable(
                walletPerAccount.get(deposit.accountId())
        ).orElse(
                new HashMap<>()
        );
        var oldWallet = Optional.ofNullable(
                wallets.get(deposit.walletId())
        ).orElse(
                new Wallet(ACCOUNT_ID, deposit.walletId(), deposit.symbol(), BigDecimal.ZERO, BigDecimal.ZERO)
        );
        var newWallet = oldWallet.addAvailable(deposit.amount());
        wallets.put(newWallet.walletId(), newWallet);
        walletPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }

    public Wallet withdrawToWallet(WithdrawFiatMoney withdraw) {
        final var wallets = Optional.ofNullable(
                walletPerAccount.get(withdraw.accountId())
        ).orElse(
                new HashMap<>()
        );
        var oldWallet = wallets.get(withdraw.walletId());
        var newWallet = oldWallet.withdrawAvailable(withdraw.amount());
        wallets.put(newWallet.walletId(), newWallet);
        walletPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }
}
