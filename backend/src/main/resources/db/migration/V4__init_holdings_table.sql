CREATE TABLE IF NOT EXISTS holdings (
    symbol VARCHAR(8) NOT NULL,
    wallet_id UUID NOT NULL REFERENCES wallets(id) ON DELETE CASCADE,
    amount NUMERIC(15, 5) NOT NULL CHECK (amount >= 0),
    average_price NUMERIC(15, 2) NOT NULL CHECK (average_price >= 0),
    PRIMARY KEY (symbol, wallet_id)
);
