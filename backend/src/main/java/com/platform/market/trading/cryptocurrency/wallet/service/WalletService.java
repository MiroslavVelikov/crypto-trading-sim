package com.platform.market.trading.cryptocurrency.wallet.service;

import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletHoldingsOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletTransactionsOutput;

import java.util.UUID;

public interface WalletService {

    WalletOutput getWalletByTraderId(UUID traderId);
    boolean traderOwnsWallet(UUID id, UUID traderId);
    WalletOutput createWallet(UUID traderId);
    WalletTransactionsOutput getWalletTransactions(UUID id, UUID traderId);
    WalletHoldingsOutput getWalletHoldings(UUID id, UUID traderId);
    TransactionOutput makeTransaction(UUID id, UUID traderId, String symbol, Double amount, Double price, TransactionType type);
    WalletOutput resetWallet(UUID id, UUID traderId);

}
