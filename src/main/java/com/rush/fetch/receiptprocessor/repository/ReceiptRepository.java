package com.rush.fetch.receiptprocessor.repository;

import com.rush.fetch.receiptprocessor.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, String> {
}