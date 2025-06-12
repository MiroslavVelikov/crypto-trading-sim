package com.platform.market.trading.cryptocurrency.transaction.service;


import com.platform.market.trading.cryptocurrency.transaction.models.Transaction;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;
import com.platform.market.trading.cryptocurrency.transaction.repository.TransactionRepository;
import com.platform.market.trading.cryptocurrency.transaction.service.convertor.TransactionServiceConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionServiceConvertor transactionServiceConvertor;

    @Override
    public List<TransactionOutput> getTransactionsFromWallet(UUID walletId) {
        return transactionRepository.getTransactionsByWalletId(walletId).stream()
            .map(transactionServiceConvertor::convertTransactionToTransactionOutput)
            .collect(Collectors.toList());
    }

    @Override
    public TransactionOutput createTransaction(UUID walletId, String symbol, Double amount, Double price,
                                               TransactionType type) {
        Transaction transaction = new Transaction(UUID.randomUUID(), walletId, symbol, amount, price, type.getType());
        return transactionRepository.createTransaction(transaction)
            ? transactionServiceConvertor.convertTransactionToTransactionOutput(transaction) : null;
    }
}
