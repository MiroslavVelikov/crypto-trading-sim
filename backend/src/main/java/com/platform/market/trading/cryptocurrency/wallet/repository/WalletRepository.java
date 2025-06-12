package com.platform.market.trading.cryptocurrency.wallet.repository;

import com.platform.market.trading.cryptocurrency.wallet.models.Wallet;

import java.util.UUID;

public interface WalletRepository {

    Wallet getWalletById(UUID id);
    Wallet getWalletByTraderId(UUID traderId);
    boolean updateWallet(Wallet updatedWallet);
    boolean createWallet(Wallet wallet);
    boolean deleteWallet(UUID id);

}
