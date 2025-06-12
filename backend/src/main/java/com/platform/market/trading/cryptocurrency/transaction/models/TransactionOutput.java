package com.platform.market.trading.cryptocurrency.transaction.models;

import lombok.Data;

import java.util.UUID;

@Data
public class TransactionOutput {

    private final UUID id;
    private final String symbol;
    private final Double amount;
    private final Double price;
    private final String transactionType;

}
