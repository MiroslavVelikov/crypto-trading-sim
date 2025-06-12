package com.platform.market.trading.cryptocurrency.wallet.models;

import com.platform.market.trading.cryptocurrency.holding.models.HoldingOutput;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WalletHoldingsOutput {

    private final UUID id;
    private final Double balance;
    private final List<HoldingOutput> transactions;

}
