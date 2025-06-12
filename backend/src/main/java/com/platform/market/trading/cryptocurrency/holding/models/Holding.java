package com.platform.market.trading.cryptocurrency.holding.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Holding {

    private final String symbol;
    private final UUID walletId;
    private Double amount;
    private Double averagePrice;

}
