package com.platform.market.trading.cryptocurrency.wallet.models;

import lombok.Data;

import java.util.UUID;

@Data
public class WalletOutput {

    private final UUID id;
    private final Double balance;

}
