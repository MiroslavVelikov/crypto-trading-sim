CREATE TABLE IF NOT EXISTS transactions (
    id UUID NOT NULL PRIMARY KEY CHECK (id <> '00000000-0000-0000-0000-000000000000'),
    wallet_id UUID NOT NULL REFERENCES wallets(id) ON DELETE CASCADE,
    symbol VARCHAR(8) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL CHECK (amount >= 0),
    price NUMERIC(15, 2) NOT NULL CHECK (price >= 0),
    transaction_type VARCHAR(4) NOT NULL CHECK (transaction_type IN ('buy', 'sell'))
);
