package com.platform.market.trading.cryptocurrency.trader.service.convertor;

import com.platform.market.trading.cryptocurrency.trader.models.Trader;
import com.platform.market.trading.cryptocurrency.trader.models.TraderOutput;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Primary
public class TraderServiceConvertorImpl implements TraderServiceConvertor{

    @Override
    public TraderOutput convertTraderToTraderOutput(Trader trader, UUID walletId) {
        return new TraderOutput(
            trader.getId(),
            trader.getUsername(),
            trader.getFirstName() + " " + trader.getLastName(),
            walletId
        );
    }
}

