package com.platform.market.trading.cryptocurrency.holding.models;

import lombok.Data;

import java.util.UUID;

@Data
public class HoldingOutput {

    private final String symbol;
    private final Double amount;
    private final Double averagePrice;

}
