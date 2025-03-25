package com.rush.fetch.receiptprocessor.service;

import com.rush.fetch.receiptprocessor.dto.ReceiptDataDTO;

public interface ReceiptProcessorService {
    String processReceiptData(ReceiptDataDTO request);
    int getReceiptPoints(String receiptId);
}
