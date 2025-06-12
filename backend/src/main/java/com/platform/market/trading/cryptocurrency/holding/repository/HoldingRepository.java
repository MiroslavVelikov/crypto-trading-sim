package com.platform.market.trading.cryptocurrency.holding.repository;

import com.platform.market.trading.cryptocurrency.holding.models.Holding;

import java.util.List;
import java.util.UUID;

public interface HoldingRepository {

    List<Holding> getHoldingsByWalletId(UUID walletId);
    Holding getHolding(String symbol, UUID walletId);
    boolean createHolding(Holding holding);
    boolean updateHolding(Holding holding);
    boolean removeHolding(String symbol, UUID walletId);

}
