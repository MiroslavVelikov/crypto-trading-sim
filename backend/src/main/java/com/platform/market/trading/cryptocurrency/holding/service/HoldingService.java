package com.platform.market.trading.cryptocurrency.holding.service;

import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;

import java.util.List;
import java.util.UUID;

public interface HoldingService {

    List<HoldingOutput> getHoldingsFromWallet(UUID walletId);
    boolean checkHoldingExistence(UUID walletId, String symbol);
    HoldingOutput updateHolding(UUID walletId, String symbol, Double amount, Double price, TransactionType type);
    HoldingOutput removeHolding(String symbol, UUID walletId);

}
