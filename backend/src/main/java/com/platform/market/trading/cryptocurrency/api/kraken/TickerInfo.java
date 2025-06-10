package com.platform.market.trading.cryptocurrency.kraken;

import lombok.Getter;

@Getter
public enum TickerInfo {

    ASK("a"),
    BID("b"),
    LAST_TRADE_CLOSED("c"),
    VOLUME("v"),
    VOLUME_WEIGHTED_AVERAGE_PRICE("p"),
    NUMBER_OF_TRADES("t"),
    LOW("l"),
    HIGH("h"),
    TODAYS_OPENING_PRICE("o");

    private final String ticker;

    TickerInfo(String ticker) {
        this.ticker = ticker;
    }

}
