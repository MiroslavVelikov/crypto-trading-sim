package com.platform.market.trading.cryptocurrency.transaction.service;

import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    List<TransactionOutput> getTransactionsFromWallet(UUID walletId);
    TransactionOutput createTransaction(UUID walletId, String symbol, Double amount, Double price, TransactionType type);

}
