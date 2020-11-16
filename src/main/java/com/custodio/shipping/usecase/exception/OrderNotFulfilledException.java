package com.custodio.shipping.usecase.exception;

public class OrderNotFulfilledException extends RuntimeException {
    public OrderNotFulfilledException(final String message) {
        super(message);
    }
}
