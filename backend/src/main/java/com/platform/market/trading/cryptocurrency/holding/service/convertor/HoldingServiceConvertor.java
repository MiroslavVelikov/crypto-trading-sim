package com.platform.market.trading.cryptocurrency.holding.service.convertor;

import com.platform.market.trading.cryptocurrency.holding.models.Holding;
import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import org.springframework.stereotype.Component;

@Component
public interface HoldingServiceConvertor {

    HoldingOutput convertHoldingToHoldingOutput(Holding holding);

}
