package com.platform.market.trading.cryptocurrency.wallet.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Wallet {

    private final UUID id;
    private final UUID traderId;
    private Double balance;

}
