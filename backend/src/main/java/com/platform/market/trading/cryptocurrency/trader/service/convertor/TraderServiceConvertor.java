package com.platform.market.trading.cryptocurrency.trader.service.convertor;

import com.platform.market.trading.cryptocurrency.trader.models.Trader;
import com.platform.market.trading.cryptocurrency.trader.models.TraderOutput;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface TraderServiceConvertor {

    TraderOutput convertTraderToTraderOutput(Trader trader, UUID walletId);

}
