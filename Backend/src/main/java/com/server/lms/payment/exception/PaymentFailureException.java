package com.server.lms.payment.exception;

public class PaymentFailureException extends RuntimeException {
    public PaymentFailureException(String message) {
        super(message);
    }
}
