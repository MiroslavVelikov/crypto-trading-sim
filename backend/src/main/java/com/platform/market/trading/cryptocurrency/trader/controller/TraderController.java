package com.platform.market.trading.cryptocurrency.trader.controller;

import com.platform.market.trading.cryptocurrency.trader.controller.request.CreateTraderRequest;
import com.platform.market.trading.cryptocurrency.trader.controller.request.LoginTraderRequest;
import com.platform.market.trading.cryptocurrency.trader.models.TraderOutput;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface TraderController {

    ResponseEntity<TraderOutput> loginTrader(@Valid @RequestBody LoginTraderRequest request);
    ResponseEntity<TraderOutput> createTrader(@Valid @RequestBody CreateTraderRequest request);

}
