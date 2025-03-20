package com.stocks.trading.exception;

public class InsufficientSharesException extends Exception {
    public InsufficientSharesException(String message) {
        super(message);
    }
}