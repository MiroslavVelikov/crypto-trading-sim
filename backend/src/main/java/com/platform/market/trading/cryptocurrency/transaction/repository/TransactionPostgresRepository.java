package com.platform.market.trading.cryptocurrency.transaction.repository;

import com.platform.market.trading.cryptocurrency.transaction.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@Primary
@AllArgsConstructor
public class TransactionPostgresRepository implements TransactionRepository {

    private static final String TRANSACTIONS_TABLE = "transactions";
    private static final String TRANSACTIONS_TABLE_ID = "id";
    private static final String TRANSACTIONS_TABLE_WALLET_ID = "wallet_id";
    private static final String TRANSACTIONS_TABLE_SYMBOL = "symbol";
    private static final String TRANSACTIONS_TABLE_AMOUNT = "amount";
    private static final String TRANSACTIONS_TABLE_PRICE = "price";
    private static final String TRANSACTIONS_TABLE_TYPE = "transaction_type";
    private static final String[] TRANSACTIONS_TABLE_COLUMNS = { "id", "wallet_id", "symbol", "amount", "price", "transaction_type" };

    private static final RowMapper<Transaction> TRANSACTION_ROW_MAPPER = (rs, rowNum) -> new Transaction(
        UUID.fromString(rs.getString(TRANSACTIONS_TABLE_ID)),
        UUID.fromString(rs.getString(TRANSACTIONS_TABLE_WALLET_ID)),
        rs.getString(TRANSACTIONS_TABLE_SYMBOL),
        Double.parseDouble(rs.getString(TRANSACTIONS_TABLE_AMOUNT)),
        Double.parseDouble(rs.getString(TRANSACTIONS_TABLE_PRICE)),
        rs.getString(TRANSACTIONS_TABLE_TYPE)
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> getTransactionsByWalletId(UUID walletId) {
        String cond = String.format("%s = ?", TRANSACTIONS_TABLE_WALLET_ID);
        String sql = String.format("SELECT %s FROM %s WHERE %s",
            String.join(", ", TRANSACTIONS_TABLE_COLUMNS), TRANSACTIONS_TABLE, cond);

        return jdbcTemplate.query(sql, TRANSACTION_ROW_MAPPER, walletId);
    }

    @Override
    public boolean createTransaction(Transaction transaction) {
        String sql = String.format("INSERT INTO %s (%s) VALUES (?, ?, ?, ?, ?, ?)",
            TRANSACTIONS_TABLE, String.join(", ", TRANSACTIONS_TABLE_COLUMNS));
        int rows = jdbcTemplate.update(sql,
            transaction.getId(),
            transaction.getWalletId(),
            transaction.getSymbol(),
            transaction.getAmount(),
            transaction.getPrice(),
            transaction.getTransactionType()
        );

        if (rows == 0) {
            log.error("Failed inserting transaction");
            return false;
        }

        return true;
    }

}
