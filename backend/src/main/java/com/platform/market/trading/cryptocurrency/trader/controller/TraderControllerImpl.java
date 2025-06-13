package com.platform.market.trading.cryptocurrency.trader.controller;

import com.platform.market.trading.cryptocurrency.trader.controller.request.CreateTraderRequest;
import com.platform.market.trading.cryptocurrency.trader.controller.request.LoginTraderRequest;
import com.platform.market.trading.cryptocurrency.trader.models.TraderOutput;
import com.platform.market.trading.cryptocurrency.trader.service.TraderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/traders")
@CrossOrigin(origins = "http://localhost:5173")
public class TraderControllerImpl implements TraderController {
    // IF YOU HAVE TIME FIX THIS CONTROLLER OUTPUTS
    @Autowired
    private TraderService traderService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<TraderOutput> loginTrader(@Valid @RequestBody LoginTraderRequest request) {
        return new ResponseEntity<>(
            traderService.authenticateTrader(request.getUsername(), request.getPassword()),
            HttpStatus.OK);
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<TraderOutput> createTrader(@Valid @RequestBody  CreateTraderRequest request) {
        return new ResponseEntity<>(traderService.createTrader(
            request.getUsername(),
            request.getFirstName(),
            request.getLastName(),
            request.getPassword()
        ), HttpStatus.CREATED);
    }

}
