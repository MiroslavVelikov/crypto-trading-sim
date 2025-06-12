package com.platform.market.trading.cryptocurrency.wallet.models;

import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WalletTransactionsOutput {

    private final UUID id;
    private final Double balance;
    private final List<TransactionOutput> transactions;

}
