package com.platform.market.trading.cryptocurrency.wallet.service.convertor;

import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.Wallet;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletHoldingsOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletTransactionsOutput;

import java.util.List;

public interface WalletServiceConvertor {

    WalletOutput convertWalletToWalletOutput(Wallet wallet);
    WalletTransactionsOutput convertWalletToTransactionsWallet(Wallet wallet, List<TransactionOutput> transactionOutputs);
    WalletHoldingsOutput convertWalletToHoldingsWallet(Wallet wallet, List<HoldingOutput> holdingOutputs);

}
