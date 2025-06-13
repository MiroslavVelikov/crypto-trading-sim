package com.platform.market.trading.cryptocurrency.wallet.repository;

import com.platform.market.trading.cryptocurrency.wallet.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@Primary
@AllArgsConstructor
public class WalletPostgresRepository implements WalletRepository {

    private static final String WALLETS_TABLE = "wallets";
    private static final String WALLETS_TABLE_ID = "id";
    private static final String WALLETS_TABLE_TRADER_ID = "trader_id";
    private static final String WALLETS_TABLE_BALANCE = "balance";
    private static final String[] WALLETS_TABLE_COLUMNS = { "id", "trader_id", "balance" };

    private static final RowMapper<Wallet> WALLET_ROW_MAPPER = (rs, rowNum) -> new Wallet(
        UUID.fromString(rs.getString(WALLETS_TABLE_ID)),
        UUID.fromString(rs.getString(WALLETS_TABLE_TRADER_ID)),
        Double.parseDouble(rs.getString(WALLETS_TABLE_BALANCE))
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Wallet getWalletById(UUID id) {
        String cond = String.format("%s = ?", WALLETS_TABLE_ID);
        String sql = String.format("SELECT %s FROM %s WHERE %s",
            String.join(", ", WALLETS_TABLE_COLUMNS), WALLETS_TABLE, cond);

        try {
            return jdbcTemplate.queryForObject(sql, WALLET_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found wallet with this id: {}", id);
            return null;
        }
    }

    @Override
    public Wallet getWalletByTraderId(UUID traderId) {
        String cond = String.format("%s = ?", WALLETS_TABLE_TRADER_ID);
        String sql = String.format("SELECT %s FROM %s WHERE %s",
            String.join(", ", WALLETS_TABLE_COLUMNS), WALLETS_TABLE, cond);

        try {
            return jdbcTemplate.queryForObject(sql, WALLET_ROW_MAPPER, traderId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found wallet with this trader id: {}", traderId);
            return null;
        }
    }

    @Override
    public boolean updateWallet(Wallet updatedWallet) {
        String cond = String.format("%s = ?", WALLETS_TABLE_ID);
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s", WALLETS_TABLE, WALLETS_TABLE_BALANCE, cond);
        int rows = jdbcTemplate.update(sql,
            updatedWallet.getBalance(),
            updatedWallet.getId()
        );

        if (rows == 0) {
            log.error("Failed updating wallet with id: {}", updatedWallet.getId());
            return false;
        }

        return true;
    }

    @Override
    public boolean createWallet(Wallet wallet) {
        String sql = String.format("INSERT INTO %s (%s) VALUES (?, ?, ?)",
            WALLETS_TABLE, String.join(", ", WALLETS_TABLE_COLUMNS));
        int rows = jdbcTemplate.update(sql,
            wallet.getId(),
            wallet.getTraderId(),
            wallet.getBalance()
        );

        if (rows == 0) {
            log.error("Failed inserting wallet");
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteWallet(UUID id) {
        try {
            String cond = String.format("%s = ?", WALLETS_TABLE_ID);
            String sql = String.format("DELETE FROM %s WHERE %s", WALLETS_TABLE, cond);
            int rows = jdbcTemplate.update(sql, id);

            if (rows == 0) {
                log.warn("No wallet found to delete with id: {}", id);
                return false;
            }

            return true;
        } catch (DataAccessException ex) {
            log.error("Error deleting wallet with id: {}", id, ex);
            return false;
        }
    }


}
