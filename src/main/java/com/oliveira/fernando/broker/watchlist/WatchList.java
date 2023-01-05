package com.oliveira.fernando.broker.watchlist;

import com.oliveira.fernando.broker.symbols.Symbol;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Introspected
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {

    private List<Symbol> symbols = new ArrayList<>();

}