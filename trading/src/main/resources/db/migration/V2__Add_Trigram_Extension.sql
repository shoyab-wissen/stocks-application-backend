-- Enable the pg_trgm extension for fuzzy search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Create indexes for better search performance
CREATE INDEX IF NOT EXISTS stocks_symbol_trgm_idx ON stocks USING gin (symbol gin_trgm_ops);
CREATE INDEX IF NOT EXISTS stocks_name_trgm_idx ON stocks USING gin (name gin_trgm_ops);