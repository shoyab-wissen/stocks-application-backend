-- Drop tables if they exist
DROP TABLE IF EXISTS user_watchlist;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS user_stock;
DROP TABLE IF EXISTS stocks;
DROP TABLE IF EXISTS bank_user;

-- Create stocks table
CREATE TABLE stocks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    symbol VARCHAR(10),
    price DECIMAL(10,2),
    quantity INTEGER,
    total_value DECIMAL(15,2),
    min DECIMAL(10,2),
    max DECIMAL(10,2),
    min_value DECIMAL(10,2),
    max_value DECIMAL(10,2),
    last_closing_price DECIMAL(10,2),
    last_updated TIMESTAMP,
    history jsonb
);

-- Create bank_user table
CREATE TABLE bank_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    is_active BOOLEAN,
    account_number INTEGER,
    balance DECIMAL(15,2),
    transaction_count INTEGER,
    pan_card VARCHAR(10)
);

-- Create user_stock table
CREATE TABLE user_stock (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES bank_user(id),
    stock_id INTEGER REFERENCES stocks(id),
    quantity INTEGER,
    average_buy_price DECIMAL(10,2),
    total_investment DECIMAL(15,2),
    current_value DECIMAL(15,2),
    profit_loss DECIMAL(15,2)
);

-- Create user_watchlist table
CREATE TABLE user_watchlist (
    user_id INTEGER REFERENCES bank_user(id),
    stock_id INTEGER REFERENCES stocks(id),
    PRIMARY KEY (user_id, stock_id)
);

-- Create transaction table
CREATE TABLE transaction (
    transaction_id SERIAL PRIMARY KEY,
    account_id VARCHAR(255),
    transaction_type VARCHAR(10),
    amount DECIMAL(15,2),
    transaction_date TIMESTAMP,
    description TEXT,
    status VARCHAR(20),
    stock_id INTEGER REFERENCES stocks(id),
    stock_qty INTEGER
);