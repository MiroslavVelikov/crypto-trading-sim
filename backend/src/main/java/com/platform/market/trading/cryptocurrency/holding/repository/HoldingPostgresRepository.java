package com.platform.market.trading.cryptocurrency.holding.repository;

import com.platform.market.trading.cryptocurrency.holding.models.Holding;
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
public class HoldingPostgresRepository implements HoldingRepository {

    private static final String HOLDINGS_TABLE = "holdings";
    private static final String HOLDINGS_TABLE_SYMBOL = "symbol";
    private static final String HOLDINGS_TABLE_WALLET_ID = "wallet_id";
    private static final String HOLDINGS_TABLE_AMOUNT = "amount";
    private static final String HOLDINGS_TABLE_AVERAGE_PRICE = "average_price";
    private static final String[] HOLDINGS_TABLE_COLUMNS = { "symbol", "wallet_id", "amount", "average_price" };

    private static final RowMapper<Holding> HOLDINGS_ROW_MAPPER = (rs, rowNum) -> new Holding(
        rs.getString(HOLDINGS_TABLE_SYMBOL),
        UUID.fromString(rs.getString(HOLDINGS_TABLE_WALLET_ID)),
        Double.parseDouble(rs.getString(HOLDINGS_TABLE_AMOUNT)),
        Double.parseDouble(rs.getString(HOLDINGS_TABLE_AVERAGE_PRICE))
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Holding> getHoldingsByWalletId(UUID walletId) {
        String cond = String.format("%s = ?", HOLDINGS_TABLE_WALLET_ID);
        String sql = String.format("SELECT %s FROM %s WHERE %s",
            String.join(", ", HOLDINGS_TABLE_COLUMNS), HOLDINGS_TABLE, cond);

        return jdbcTemplate.query(sql, HOLDINGS_ROW_MAPPER, walletId);
    }

    @Override
    public Holding getHolding(String symbol, UUID walletId) {
        String cond = String.format("%s = ? AND %s = ?", HOLDINGS_TABLE_SYMBOL, HOLDINGS_TABLE_WALLET_ID);
        String sql = String.format("SELECT %s FROM %s WHERE %s",
            String.join(", ", HOLDINGS_TABLE_COLUMNS), HOLDINGS_TABLE, cond);

        return jdbcTemplate.queryForObject(sql, HOLDINGS_ROW_MAPPER, symbol, walletId);
    }

    @Override
    public boolean createHolding(Holding holding) {
        String sql = String.format("INSERT INTO %s (%s) VALUES (?, ?, ?, ?)",
            HOLDINGS_TABLE, String.join(", ", HOLDINGS_TABLE_COLUMNS));
        int rows = jdbcTemplate.update(sql,
            holding.getSymbol(),
            holding.getWalletId(),
            holding.getAmount(),
            holding.getAveragePrice()
        );

        if (rows == 0) {
            log.error("Failed inserting holding");
            return false;
        }

        return true;
    }

    @Override
    public boolean updateHolding(Holding holding) {
        String cond = String.format("%s = ? AND %s = ?", HOLDINGS_TABLE_SYMBOL, HOLDINGS_TABLE_WALLET_ID);
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s", HOLDINGS_TABLE, HOLDINGS_TABLE_AMOUNT, HOLDINGS_TABLE_AVERAGE_PRICE, cond);
        int rows = jdbcTemplate.update(sql,
            holding.getAmount(),
            holding.getAveragePrice()
        );

        if (rows == 0) {
            log.error("Failed updating holding ({}) from wallet with id: {}", holding.getSymbol(), holding.getWalletId());
            return false;
        }

        return true;
    }

    @Override
    public boolean removeHolding(String symbol, UUID walletId) {
        String cond = String.format("%s = ? AND %s = ?", HOLDINGS_TABLE_SYMBOL, HOLDINGS_TABLE_WALLET_ID);
        String sql = String.format("DELETE FROM %s WHERE %s", HOLDINGS_TABLE, cond);
        int rowsAffected = jdbcTemplate.update(sql, symbol, walletId);

        if (rowsAffected == 0) {
            log.warn("No holding found for symbol '{}' and walletId '{}'", symbol, walletId);
            return false;
        }

        return true;
    }

}
