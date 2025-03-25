package com.rush.fetch.receiptprocessor.customexception;

public class ReceiptIDNotFoundException extends RuntimeException {
    public ReceiptIDNotFoundException(String message) {
        super(message);
    }
}