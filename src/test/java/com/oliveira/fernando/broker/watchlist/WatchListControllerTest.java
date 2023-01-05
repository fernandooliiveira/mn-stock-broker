package com.oliveira.fernando.broker.watchlist;

import com.oliveira.fernando.broker.data.InMemoryAccountStore;
import com.oliveira.fernando.broker.symbols.Symbol;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class WatchListControllerTest {

    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);

    @Inject
    @Client("/account/watchlist")
    HttpClient client;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @Test
    void canUpdateWatchListForTestAccount() {
        var symbols = Stream.of("AAPL", "GOOGL", "MSFT").map(Symbol::new).toList();
        final var request = HttpRequest.PUT("/", new WatchList(symbols)).accept(MediaType.APPLICATION_JSON);
        final HttpResponse<Object> added = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, added.getStatus());
        Assertions.assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols());
    }

    private void givenWatchListForAccountsExists() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSTF")
                .map(Symbol::new)
                .toList()
        ));
    }

    @Test
    void canDeleteWatchListForTestAccount() {
        givenWatchListForAccountsExists();
        Assertions.assertFalse(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        var deleted = client.toBlocking().exchange(HttpRequest.DELETE("/"));
        LOG.debug("STATUS: {}", deleted.getStatus());
        LOG.debug("STATUS: {}", deleted.getStatus().getCode());

        assertEquals(HttpStatus.NO_CONTENT, deleted.getStatus());
        Assertions.assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

    }


}
