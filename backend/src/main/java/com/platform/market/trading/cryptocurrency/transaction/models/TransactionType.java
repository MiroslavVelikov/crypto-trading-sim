package com.platform.market.trading.cryptocurrency.transaction.models;

import lombok.Getter;

public enum TransactionType {
    BUY("buy"),
    SELL("sell");

    @Getter
    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

}
