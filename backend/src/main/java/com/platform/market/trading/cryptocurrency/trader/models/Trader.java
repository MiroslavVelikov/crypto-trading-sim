package com.platform.market.trading.cryptocurrency.trader.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Trader {

    private final UUID id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;

}
