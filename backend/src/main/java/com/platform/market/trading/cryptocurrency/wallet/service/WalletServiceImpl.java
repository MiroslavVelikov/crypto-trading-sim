package com.platform.market.trading.cryptocurrency.wallet.service;

import com.platform.market.trading.cryptocurrency.common.exception.InvalidHoldingAmount;
import com.platform.market.trading.cryptocurrency.cryptocurrency.CryptoType;
import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import com.platform.market.trading.cryptocurrency.holding.service.HoldingService;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;
import com.platform.market.trading.cryptocurrency.transaction.service.TransactionService;
import com.platform.market.trading.cryptocurrency.wallet.models.Wallet;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletHoldingsOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletTransactionsOutput;
import com.platform.market.trading.cryptocurrency.wallet.repository.WalletRepository;
import com.platform.market.trading.cryptocurrency.wallet.service.convertor.WalletServiceConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Primary
public class WalletServiceImpl implements WalletService {

    private static final Double DEFAULT_BALANCE = 10_000.0;

    @Autowired
    private WalletServiceConvertor walletServiceConvertor;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private HoldingService holdingService;

    @Override
    public WalletOutput getWalletByTraderId(UUID traderId) {
        Wallet wallet = walletRepository.getWalletByTraderId(traderId);
        return wallet != null
            ? walletServiceConvertor.convertWalletToWalletOutput(wallet) : null;
    }

    @Override
    public boolean traderOwnsWallet(UUID id, UUID traderId) {
        Wallet wallet = walletRepository.getWalletById(id);
        return wallet != null && wallet.getTraderId().equals(traderId);
    }

    @Override
    public WalletOutput createWallet(UUID traderId) {
        Wallet wallet = new Wallet(UUID.randomUUID(), traderId, DEFAULT_BALANCE);
        return walletRepository.createWallet(wallet)
            ? walletServiceConvertor.convertWalletToWalletOutput(wallet) : null;
    }

    @Override
    public WalletTransactionsOutput getWalletTransactions(UUID id, UUID traderId) {
        if (!traderOwnsWallet(id, traderId)) {
            return null;
        }

        Wallet wallet = validateWallet(id, traderId);
        if (wallet == null) {
            return null;
        }

        return walletServiceConvertor.convertWalletToTransactionsWallet(
            wallet,
            transactionService.getTransactionsFromWallet(id)
            );
    }

    @Override
    public WalletHoldingsOutput getWalletHoldings(UUID id, UUID traderId) {
        if (!traderOwnsWallet(id, traderId)) {
            return null;
        }

        Wallet wallet = validateWallet(id, traderId);
        if (wallet == null) {
            return null;
        }

        return walletServiceConvertor.convertWalletToHoldingsWallet(
            wallet,
            holdingService.getHoldingsFromWallet(id)
        );
    }

    @Override
    public TransactionOutput makeTransaction(UUID id, UUID traderId, String symbol, Double amount, Double price, TransactionType type) {
        if (!traderOwnsWallet(id, traderId)) {
            return null;
        }

        if (!symbolValidation(symbol)) {
            return null;
        }

        Wallet wallet = walletRepository.getWalletById(id);
        if (type.equals(TransactionType.BUY) && wallet.getBalance() < price * amount) {
            return null;
        }

        try {
            HoldingOutput holdingOutput = holdingService.updateHolding(id, symbol, amount, price, type);
            if (holdingOutput == null) {
                return null;
            }
        } catch (InvalidHoldingAmount e) {
            log.error("Failed creating holding because of invalid holding amount with symbol {} in wallet with id: {}",
                symbol, id);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating holding with symbol {} in wallet with id: {}",
                symbol, id);
            return null;
        }

        switch (type) {
            case TransactionType.BUY -> wallet.setBalance(wallet.getBalance() - price * amount);
            case TransactionType.SELL -> wallet.setBalance(wallet.getBalance() + price * amount);
        }
        walletRepository.updateWallet(wallet);
        return transactionService.createTransaction(id, symbol, amount, price, type);
    }

    @Override
    public WalletOutput resetWallet(UUID id, UUID traderId) {
        if (!traderOwnsWallet(id, traderId)) {
            return null;
        }

        Wallet wallet = validateWallet(id, traderId);
        if (wallet == null) {
            return null;
        }

        return walletRepository.deleteWallet(id) ? createWallet(traderId) : null;
    }

    private Wallet getWalletById(UUID id) {
        return walletRepository.getWalletById(id);
    }

    private Wallet validateWallet(UUID id, UUID traderId) {
        Wallet wallet = getWalletById(id);

        return wallet != null && wallet.getTraderId().equals(traderId) ? wallet : null;
    }

    private boolean symbolValidation(String symbol) {
        return CryptoType.getAllSymbols().contains(symbol);
    }

}
