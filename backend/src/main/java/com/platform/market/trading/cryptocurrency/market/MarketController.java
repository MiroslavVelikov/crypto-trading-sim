package com.platform.market.trading.cryptocurrency.market;

import com.platform.market.trading.cryptocurrency.api.kraken.KrakenWsApp;
import com.platform.market.trading.cryptocurrency.cryptocurrency.Cryptocurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/market/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MarketController {

    @GetMapping("/load")
    public Collection<Cryptocurrency> loadMarket() {
        return KrakenWsApp.cryptocurrencyMap().values();
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Cryptocurrency> getCurrencyBySymbol(@PathVariable String symbol) {
        System.out.println(symbol);
        Cryptocurrency cryptocurrency = KrakenWsApp.getCryptocurrencyBySymbol(symbol + "/USD");
        System.out.println(cryptocurrency);
        return cryptocurrency != null
            ? new ResponseEntity<>(cryptocurrency, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
