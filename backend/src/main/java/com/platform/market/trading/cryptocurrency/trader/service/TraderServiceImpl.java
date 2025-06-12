package com.platform.market.trading.cryptocurrency.trader.service;

import com.platform.market.trading.cryptocurrency.trader.service.convertor.TraderServiceConvertor;
import com.platform.market.trading.cryptocurrency.trader.models.Trader;
import com.platform.market.trading.cryptocurrency.trader.models.TraderOutput;
import com.platform.market.trading.cryptocurrency.trader.repository.TraderRepository;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Primary
public class TraderServiceImpl implements TraderService {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private TraderServiceConvertor traderConvertor;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private WalletService walletService;

    @Override
    public TraderOutput getTraderByID(UUID id) {
        Trader trader = traderRepository.getTraderByID(id);
        if (trader != null) {
            WalletOutput wallet = walletService.getWalletByTraderId(trader.getId());
            return wallet != null
                ? traderConvertor.convertTraderToTraderOutput(trader, wallet.getId()) : null;
        }

        return null;
    }

    @Override
    public TraderOutput getTraderByUsername(String username) {
        Trader trader = traderRepository.getTraderByUsername(username);
        if (trader != null) {
            WalletOutput wallet = walletService.getWalletByTraderId(trader.getId());
            return wallet != null
                ? traderConvertor.convertTraderToTraderOutput(trader, wallet.getId()) : null;
        }

        return null;
    }

    @Override
    public TraderOutput authenticateTrader(String username, String password) {
        Trader trader = traderRepository.getTraderByUsername(username);
        if (trader != null && encoder.matches(password, trader.getPassword())) {
            WalletOutput wallet = walletService.getWalletByTraderId(trader.getId());
            return wallet != null
                ? traderConvertor.convertTraderToTraderOutput(trader, wallet.getId()) : null;
        }

        return null;
    }

    @Override
    public TraderOutput createTrader(String username, String firstName, String lastName, String password) {
        UUID traderId = UUID.randomUUID();
        String hashedPassword = encoder.encode(password);

        Trader newTrader = new Trader(
            traderId,
            username,
            firstName,
            lastName,
            hashedPassword
        );

        if (traderRepository.createTrader(newTrader)) {
            WalletOutput wallet = walletService.createWallet(traderId);
            return traderConvertor.convertTraderToTraderOutput(newTrader, wallet.getId());
        }

        return null;
    }

}
