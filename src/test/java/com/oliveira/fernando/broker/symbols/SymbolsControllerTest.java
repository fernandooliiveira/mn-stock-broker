package com.oliveira.fernando.broker.symbols;

import com.oliveira.fernando.broker.data.InMemoryStore;
import com.oliveira.fernando.broker.symbols.Symbol;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class SymbolsControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    void setup() {
        inMemoryStore.initializeWith(10);
    }

    @Test
    void symbolsEndpointsReturnsListOfSymbols() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbolsEndpointsReturnsTheCorrectSymbol() {
        var testSymbol = new Symbol("FER");
        inMemoryStore.getSymbols().put(testSymbol.value(), testSymbol);
        var response = client.toBlocking().exchange("/" + testSymbol.value(), Symbol.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(testSymbol, response.getBody().get());
    }

    @Test
    void symbolsEndpointsReturnsListOfSymbolTakingQueryParameterIntoAccount() {
        var max10 = client.toBlocking().exchange("/filter?max=10", JsonNode.class);
        LOG.debug("Max 10: {}", max10.getBody());
        assertEquals(HttpStatus.OK, max10.getStatus());
        assertEquals(10, max10.getBody().get().size());

        var offset = client.toBlocking().exchange("/filter?offset=7", JsonNode.class);
        LOG.debug("Offset 7: {}", offset.getBody());
        assertEquals(HttpStatus.OK, offset.getStatus());
        assertEquals(3, offset.getBody().get().size());

        var max2Offset7 = client.toBlocking().exchange("/filter?max=2&offset=7", JsonNode.class);
        LOG.debug("Max 2, Offset 7: {}", max2Offset7.getBody().get());
        assertEquals(HttpStatus.OK, max2Offset7.getStatus());
        assertEquals(2, max2Offset7.getBody().get().size());
    }

}
