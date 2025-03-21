-- Clear existing data
DELETE FROM user_watchlist;
DELETE FROM transaction;
DELETE FROM user_stock;
DELETE FROM stocks;
DELETE FROM bank_user;

-- Insert dummy stocks
INSERT INTO stocks (id, name, symbol, price, quantity, total_value, min, max, min_value, max_value, last_closing_price, last_updated) 
VALUES 
(1, 'Apple Inc.', 'AAPL', 175.50, 1000, 175500.00, 170, 180, 170.25, 179.75, 174.50, CURRENT_TIMESTAMP),
(2, 'Microsoft Corporation', 'MSFT', 285.25, 800, 228200.00, 280, 290, 280.50, 289.75, 284.50, CURRENT_TIMESTAMP),
(3, 'Amazon.com Inc.', 'AMZN', 130.75, 1200, 156900.00, 128, 133, 128.25, 132.75, 129.90, CURRENT_TIMESTAMP),
(4, 'Tesla Inc.', 'TSLA', 245.30, 600, 147180.00, 240, 250, 240.25, 249.75, 244.50, CURRENT_TIMESTAMP),
(5, 'NVIDIA Corporation', 'NVDA', 420.15, 400, 168060.00, 415, 425, 415.25, 424.75, 419.50, CURRENT_TIMESTAMP);

-- Insert dummy users
INSERT INTO bank_user (id, name, email, password, is_active, account_number, balance, transaction_count, pan_card) 
VALUES 
(1, 'John Doe', 'john.doe@example.com', 'password123', true, 1001, 50000.00, 5, 'ABCDE1234F'),
(2, 'Jane Smith', 'jane.smith@example.com', 'password456', true, 1002, 75000.00, 3, 'FGHIJ5678K'),
(3, 'Bob Wilson', 'bob.wilson@example.com', 'password789', true, 1003, 100000.00, 7, 'KLMNO9012P');

-- Insert user stocks (holdings)
INSERT INTO user_stock (id, user_id, stock_id, quantity, average_buy_price, total_investment, current_value, profit_loss) 
VALUES 
-- John Doe's holdings
(1, 1, 1, 10, 170.50, 1705.00, 1755.00, 50.00),  -- Apple
(2, 1, 2, 5, 280.00, 1400.00, 1426.25, 26.25),   -- Microsoft
(3, 1, 3, 15, 128.50, 1927.50, 1961.25, 33.75),  -- Amazon

-- Jane Smith's holdings
(4, 2, 2, 8, 282.50, 2260.00, 2282.00, 22.00),   -- Microsoft
(5, 2, 4, 12, 242.00, 2904.00, 2943.60, 39.60),  -- Tesla
(6, 2, 5, 6, 418.00, 2508.00, 2520.90, 12.90),   -- NVIDIA

-- Bob Wilson's holdings
(7, 3, 1, 20, 172.50, 3450.00, 3510.00, 60.00),  -- Apple
(8, 3, 3, 25, 129.00, 3225.00, 3268.75, 43.75),  -- Amazon
(9, 3, 5, 10, 419.00, 4190.00, 4201.50, 11.50);  -- NVIDIA

-- Insert watchlist entries
INSERT INTO user_watchlist (user_id, stock_id) 
VALUES 
-- John Doe's watchlist
(1, 4),  -- Tesla
(1, 5),  -- NVIDIA

-- Jane Smith's watchlist
(2, 1),  -- Apple
(2, 3),  -- Amazon

-- Bob Wilson's watchlist
(3, 2),  -- Microsoft
(3, 4);  -- Tesla

-- Insert sample transactions
INSERT INTO transaction (transaction_id, account_id, transaction_type, amount, transaction_date, description, status, stock_id, stock_qty) 
VALUES 
-- John Doe's transactions
(1, '1', 'BUY', 1705.00, '2024-01-15 10:30:00', 'Bought 10 shares of AAPL', 'COMPLETED', 1, 10),
(2, '1', 'BUY', 1400.00, '2024-01-16 11:15:00', 'Bought 5 shares of MSFT', 'COMPLETED', 2, 5),
(3, '1', 'BUY', 1927.50, '2024-01-17 14:20:00', 'Bought 15 shares of AMZN', 'COMPLETED', 3, 15),

-- Jane Smith's transactions
(4, '2', 'BUY', 2260.00, '2024-01-15 09:45:00', 'Bought 8 shares of MSFT', 'COMPLETED', 2, 8),
(5, '2', 'BUY', 2904.00, '2024-01-16 13:30:00', 'Bought 12 shares of TSLA', 'COMPLETED', 4, 12),
(6, '2', 'BUY', 2508.00, '2024-01-17 15:45:00', 'Bought 6 shares of NVDA', 'COMPLETED', 5, 6),

-- Bob Wilson's transactions
(7, '3', 'BUY', 3450.00, '2024-01-15 10:00:00', 'Bought 20 shares of AAPL', 'COMPLETED', 1, 20),
(8, '3', 'BUY', 3225.00, '2024-01-16 11:30:00', 'Bought 25 shares of AMZN', 'COMPLETED', 3, 25),
(9, '3', 'BUY', 4190.00, '2024-01-17 14:15:00', 'Bought 10 shares of NVDA', 'COMPLETED', 5, 10);

-- Set the sequence values after inserting data
SELECT setval('stocks_id_seq', (SELECT MAX(id) FROM stocks));
SELECT setval('bank_user_id_seq', (SELECT MAX(id) FROM bank_user));
SELECT setval('user_stock_id_seq', (SELECT MAX(id) FROM user_stock));
SELECT setval('transaction_transaction_id_seq', (SELECT MAX(transaction_id) FROM transaction));