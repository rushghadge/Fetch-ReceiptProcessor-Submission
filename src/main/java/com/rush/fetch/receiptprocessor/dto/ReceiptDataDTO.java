package com.rush.fetch.receiptprocessor.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ReceiptDataDTO {
    //Validate input fields from request body
    @NotBlank(message = "Retailer name is required")
    public String retailer;
    @NotBlank(message = "Purchase date is required (format: yyyy-MM-dd)")
    public String purchaseDate;
    @NotBlank(message = "Purchase time is required (format: HH:mm)")
    public String purchaseTime;
    @NotEmpty(message = "At least one item is required")
    public List<ItemDTO> items;
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Total must be in decimal format (e.g., 12.34)")

    public String total;
    public String getRetailer() {
        return retailer;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public String getTotal() {
        return total;
    }



    public static class ItemDTO {
        @NotBlank(message = "Item shortDescription is required")
        public String shortDescription;
        @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Item price must be in decimal format (e.g., 1.25)")
        public String price;
    }
}