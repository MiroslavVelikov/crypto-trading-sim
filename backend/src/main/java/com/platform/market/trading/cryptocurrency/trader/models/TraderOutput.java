package com.platform.market.trading.cryptocurrency.trader.models;

import lombok.Data;

import java.util.UUID;

@Data
public class TraderOutput {

    private final UUID id;
    private final String username;
    private final String fullName;
    private final UUID walletId;

}
