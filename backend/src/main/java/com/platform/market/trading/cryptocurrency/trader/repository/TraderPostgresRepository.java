package com.platform.market.trading.cryptocurrency.trader.repository;

import com.platform.market.trading.cryptocurrency.trader.models.Trader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.UUID;

@Slf4j
@Repository
@Primary
@AllArgsConstructor
public class TraderPostgresRepository implements TraderRepository {

    private static final String TRADERS_TABLE = "traders";
    private static final String TRADERS_TABLE_ID = "id";
    private static final String TRADERS_TABLE_USERNAME = "username";
    private static final String TRADERS_TABLE_FIRST_NAME = "first_name";
    private static final String TRADERS_TABLE_LAST_NAME = "last_name";
    private static final String TRADERS_TABLE_PASSWORD = "password";
    private static final String[] TRADERS_TABLE_COLUMNS = {"id", "username", "first_name", "last_name", "password"};

    private static final RowMapper<Trader> TRADER_ROW_MAPPER = (rs, rowNum) -> new Trader(
        UUID.fromString(rs.getString(TRADERS_TABLE_ID)),
        rs.getString(TRADERS_TABLE_USERNAME),
        rs.getString(TRADERS_TABLE_FIRST_NAME),
        rs.getString(TRADERS_TABLE_LAST_NAME),
        rs.getString(TRADERS_TABLE_PASSWORD)
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Trader getTraderByID(UUID id) {
        String cond = String.format("%s = ?", TRADERS_TABLE_ID);
        String sql =
            String.format("SELECT %s FROM %s WHERE %s", String.join(", ", TRADERS_TABLE_COLUMNS), TRADERS_TABLE, cond);

        try {
            return jdbcTemplate.queryForObject(sql, TRADER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found trader with this id: {}", id);
            return null;
        }
    }

    @Override
    public Trader getTraderByUsername(String username) {
        String cond = String.format("%s = ?", TRADERS_TABLE_USERNAME);
        String sql = String.format("SELECT %s FROM %s WHERE %s",
            String.join(", ", TRADERS_TABLE_COLUMNS), TRADERS_TABLE, cond);

        try {
            return jdbcTemplate.queryForObject(sql, TRADER_ROW_MAPPER, username);
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found trader with this username: {}", username);
            return null;
        }
    }

    @Override
    public boolean createTrader(Trader trader) {
        final String sql = String.format("INSERT INTO %s (%s) VALUES (?, ?, ?, ?, ?)",
            TRADERS_TABLE, String.join(", ", TRADERS_TABLE_COLUMNS));

        int rows = 0;
        try {
            rows = jdbcTemplate.update(sql,
                trader.getId(),
                trader.getUsername(),
                trader.getFirstName(),
                trader.getLastName(),
                trader.getPassword()
            );
        } catch (DataIntegrityViolationException e) {
            // Handle unique constraint violation
            log.error("Unique constraint violation for trader with this username: {}", trader.getUsername());
            return false;
        } catch (DataAccessException e) {
            log.error("Error inserting trader: {}", trader, e);
            return false;
        }

        if (rows == 0) {
            log.error("Failed inserting trader: {}", trader);
            return false;
        }

        return true;
    }

}
