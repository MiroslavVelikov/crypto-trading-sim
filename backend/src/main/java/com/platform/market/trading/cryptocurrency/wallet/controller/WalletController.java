package com.platform.market.trading.cryptocurrency.wallet.controller;

import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.wallet.controller.request.CreateTransactionRequest;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletHoldingsOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletTransactionsOutput;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface WalletController {

    ResponseEntity<WalletTransactionsOutput> getTransactions(UUID traderId, UUID walletId);
    ResponseEntity<WalletHoldingsOutput> getHoldings(UUID traderId, UUID walletId);
    ResponseEntity<TransactionOutput> buyHolding(
        UUID traderId,
        UUID walletId,
        CreateTransactionRequest transactionRequest);
    ResponseEntity<TransactionOutput> sellHolding(
        UUID traderId,
        UUID walletId,
        CreateTransactionRequest transactionRequest);
    ResponseEntity<WalletOutput> resetWallet(UUID traderId, UUID walletId);

}
