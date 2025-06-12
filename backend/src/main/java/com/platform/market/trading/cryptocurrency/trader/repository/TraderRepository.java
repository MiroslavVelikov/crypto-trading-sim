package com.platform.market.trading.cryptocurrency.trader.repository;

import com.platform.market.trading.cryptocurrency.trader.models.Trader;

import java.util.UUID;

public interface TraderRepository {

    Trader getTraderByID(UUID id);
    Trader getTraderByUsername(String username);
    boolean createTrader(Trader trader);

}
