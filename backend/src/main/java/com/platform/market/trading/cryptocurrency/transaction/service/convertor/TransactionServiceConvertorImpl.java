package com.platform.market.trading.cryptocurrency.transaction.service.convertor;

import com.platform.market.trading.cryptocurrency.transaction.models.Transaction;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceConvertorImpl implements TransactionServiceConvertor{

    @Override
    public TransactionOutput convertTransactionToTransactionOutput(Transaction transaction) {
        return new TransactionOutput(
            transaction.getId(),
            transaction.getSymbol(),
            transaction.getAmount(),
            transaction.getPrice(),
            transaction.getTransactionType()
        );
    }

}
