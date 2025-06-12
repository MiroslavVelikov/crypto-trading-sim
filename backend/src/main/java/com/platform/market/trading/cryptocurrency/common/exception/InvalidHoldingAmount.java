package com.platform.market.trading.cryptocurrency.common.exception;

public class InvalidHoldingAmount extends RuntimeException {

    public InvalidHoldingAmount(String message) {
        super(message);
    }

    public InvalidHoldingAmount(String message, Throwable cause) {
        super(message, cause);
    }

}
