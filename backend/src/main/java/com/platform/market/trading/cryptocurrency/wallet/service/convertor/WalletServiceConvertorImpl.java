package com.platform.market.trading.cryptocurrency.wallet.service.convertor;

import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.Wallet;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletHoldingsOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletTransactionsOutput;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class WalletServiceConvertorImpl implements WalletServiceConvertor {


    @Override
    public WalletOutput convertWalletToWalletOutput(Wallet wallet) {
        return new WalletOutput(
            wallet.getId(),
            wallet.getBalance()
        );
    }

    @Override
    public WalletTransactionsOutput convertWalletToTransactionsWallet(Wallet wallet, List<TransactionOutput> transactionOutputs) {
        return new WalletTransactionsOutput(
            wallet.getId(),
            wallet.getBalance(),
            transactionOutputs
        );
    }

    @Override
    public WalletHoldingsOutput convertWalletToHoldingsWallet(Wallet wallet, List<HoldingOutput> holdingOutputs) {
        return new WalletHoldingsOutput(
            wallet.getId(),
            wallet.getBalance(),
            holdingOutputs
        );
    }
}
