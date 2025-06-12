package com.platform.market.trading.cryptocurrency.holding.service;

import com.platform.market.trading.cryptocurrency.common.exception.InvalidHoldingAmount;
import com.platform.market.trading.cryptocurrency.holding.models.Holding;
import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import com.platform.market.trading.cryptocurrency.holding.repository.HoldingRepository;
import com.platform.market.trading.cryptocurrency.holding.service.convertor.HoldingServiceConvertor;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
public class HoldingServiceImpl implements HoldingService {

    private static final String INVALID_HOLDING_AMOUNT_MESSAGE = "Not enough amount of symbol (%s)";

    @Autowired
    private HoldingServiceConvertor holdingServiceConvertor;

    @Autowired
    private HoldingRepository holdingRepository;

    @Override
    public List<HoldingOutput> getHoldingsFromWallet(UUID walletId) {
        return holdingRepository.getHoldingsByWalletId(walletId).stream()
            .map(holdingServiceConvertor::convertHoldingToHoldingOutput)
            .collect(Collectors.toList());
    }

    @Override
    public boolean checkHoldingExistence(UUID walletId, String symbol) {
        return holdingRepository.getHolding(symbol, walletId) != null;
    }

    @Override
    public HoldingOutput updateHolding(UUID walletId, String symbol, Double amount, Double price,
                                       TransactionType type) {
        Holding holding = holdingRepository.getHolding(symbol, walletId);
        if (holding == null) {
            if (type.equals(TransactionType.BUY)) {
                holding = new Holding(symbol, walletId, amount, price);
                return holdingRepository.createHolding(holding)
                    ? holdingServiceConvertor.convertHoldingToHoldingOutput(holding) : null;
            } else {
                throw new InvalidHoldingAmount(
                    String.format(INVALID_HOLDING_AMOUNT_MESSAGE, symbol)
                );
            }
        }

        switch (type) {
            case TransactionType.BUY -> buyHolding(holding, amount, price);
            case TransactionType.SELL -> sellHolding(holding, amount, price);
        }

        return holdingServiceConvertor.convertHoldingToHoldingOutput(holding);
    }

    @Override
    public HoldingOutput removeHolding(String symbol, UUID walletId) {
        Holding holding = holdingRepository.getHolding(symbol, walletId);

        return holdingRepository.removeHolding(symbol, walletId)
            ? holdingServiceConvertor.convertHoldingToHoldingOutput(holding) : null;
    }

    private void buyHolding(Holding holding, Double amount, Double price) {
        holding.setAmount(holding.getAmount() + amount);
        holding.setAveragePrice((holding.getAveragePrice() + price) / 2);

    }

    private void sellHolding(Holding holding, Double amount, Double price) {
        if (holding.getAmount() < amount) {
            throw new InvalidHoldingAmount(
                String.format(INVALID_HOLDING_AMOUNT_MESSAGE, holding.getSymbol())
            );
        }

        holding.setAmount(holding.getAmount() - amount);
        holding.setAveragePrice((holding.getAveragePrice() + price) / 2);

    }

}
