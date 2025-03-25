package com.rush.fetch.receiptprocessor.controller;
import com.rush.fetch.receiptprocessor.customexception.ReceiptIDNotFoundException;
import com.rush.fetch.receiptprocessor.dto.ReceiptDataDTO;
import com.rush.fetch.receiptprocessor.dto.ReceiptIdDTO;
import com.rush.fetch.receiptprocessor.dto.ReceiptPointsDTO;
import com.rush.fetch.receiptprocessor.service.ReceiptProcessorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptProcessorService receiptService;
    public ReceiptController(ReceiptProcessorService receiptService) {
        this.receiptService = receiptService;
    }
    @PostMapping("/process")
    public ResponseEntity<ReceiptIdDTO> processReceipt(@Valid @RequestBody ReceiptDataDTO request) {
        String processedid = receiptService.processReceiptData(request);
        return ResponseEntity.ok(new ReceiptIdDTO(processedid));
    }
    @GetMapping("/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable String id) {
        try {
            int receiptPoints = receiptService.getReceiptPoints(id);
            return ResponseEntity.ok(new ReceiptPointsDTO(receiptPoints));
        }catch (ReceiptIDNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        }

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return ResponseEntity.badRequest().body(errorMsg);
    }


}
