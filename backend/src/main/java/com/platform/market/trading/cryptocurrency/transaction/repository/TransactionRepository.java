package com.platform.market.trading.cryptocurrency.transaction.repository;

import com.platform.market.trading.cryptocurrency.transaction.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    List<Transaction> getTransactionsByWalletId(UUID walletId);
    boolean createTransaction(Transaction transaction);

}
