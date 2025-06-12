package com.platform.market.trading.cryptocurrency.transaction.service.convertor;

import com.platform.market.trading.cryptocurrency.transaction.models.Transaction;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import org.springframework.stereotype.Component;

@Component
public interface TransactionServiceConvertor {

    TransactionOutput convertTransactionToTransactionOutput(Transaction transaction);

}
