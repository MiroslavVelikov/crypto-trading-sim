package com.platform.market.trading.cryptocurrency.market;

import com.platform.market.trading.cryptocurrency.KrakenWsApp;
import com.platform.market.trading.cryptocurrency.cryptocurrency.Cryptocurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/market/public")
@RequiredArgsConstructor
public class MarketController {

    @GetMapping("/load")
    public Collection<Cryptocurrency> loadMarket() {
        return KrakenWsApp.cryptocurrencyMap().values();
    }

}
