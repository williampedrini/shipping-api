package com.moteefe.shipping.usecase.exception;

public class OrderNotFulfilledException extends RuntimeException {
    public OrderNotFulfilledException(final String message) {
        super(message);
    }
}
