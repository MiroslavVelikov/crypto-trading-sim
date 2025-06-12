package com.platform.market.trading.cryptocurrency.trader.service;

import com.platform.market.trading.cryptocurrency.trader.models.TraderOutput;

import java.util.UUID;

public interface TraderService {

    TraderOutput getTraderByID(UUID id);
    TraderOutput getTraderByUsername(String username);
    TraderOutput authenticateTrader(String username, String password);
    TraderOutput createTrader(String username, String firstName, String lastName, String password);

}
