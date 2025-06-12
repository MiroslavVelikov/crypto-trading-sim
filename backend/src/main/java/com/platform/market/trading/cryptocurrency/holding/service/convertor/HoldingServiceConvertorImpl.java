package com.platform.market.trading.cryptocurrency.holding.service.convertor;

import com.platform.market.trading.cryptocurrency.holding.models.Holding;
import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class HoldingServiceConvertorImpl implements HoldingServiceConvertor {

    @Override
    public HoldingOutput convertHoldingToHoldingOutput(Holding holding) {
        return new HoldingOutput(
            holding.getSymbol(),
            holding.getAmount(),
            holding.getAveragePrice()
        );
    }

}
