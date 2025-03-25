package com.rush.fetch.receiptprocessor.service.impl;


import com.rush.fetch.receiptprocessor.customexception.ReceiptIDNotFoundException;
import com.rush.fetch.receiptprocessor.dto.ReceiptDataDTO;
import com.rush.fetch.receiptprocessor.model.Receipt;
import com.rush.fetch.receiptprocessor.repository.ReceiptRepository;
import com.rush.fetch.receiptprocessor.service.ReceiptProcessorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptProcessorServiceImpl implements ReceiptProcessorService {

    private final ReceiptRepository receiptRepository;

    public ReceiptProcessorServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public String processReceiptData(ReceiptDataDTO request) {
        //Create receipt object- set parameters of receipt from requestdto object
        Receipt receipt = new Receipt();
        receipt.setRetailer(request.retailer);
        receipt.setPurchaseDate(request.purchaseDate);
        receipt.setPurchaseTime(request.purchaseTime);
        receipt.setTotal(request.total);
        List<Receipt.Item> itemList = new ArrayList<>();
        for (ReceiptDataDTO.ItemDTO dtoItem : request.items) {
            Receipt.Item item = new Receipt.Item();
            item.setShortDescription(dtoItem.shortDescription);
            item.setPrice(dtoItem.price);
            itemList.add(item);
        }

        receipt.setItems(itemList);
        //persist receipt object in database
        receiptRepository.save(receipt);
        //get the ID and return response
        return receipt.getId();
    }

    @Override
    public int getReceiptPoints(String receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new ReceiptIDNotFoundException("Receipt ID not found: " + receiptId));
        if (receipt != null) {
            return calculateReceiptPoints(receipt);
        } else {

            return 0;
        }
    }

    private int calculateReceiptPoints(Receipt receipt) {

        int calPoints = 0;

        // count alphanumeric characters in retailer name
        int retailerNameCount = 0;
        for (char c : receipt.getRetailer().toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                retailerNameCount++;
            }
        }
        //update calPoints
        calPoints += retailerNameCount;

        //  Round total
        String totalStr = receipt.getTotal();
        double total = Double.parseDouble(totalStr);

        boolean isRoundDollar = total == Math.floor(total);
        // if total 100.00 -> math.floor(100.0) - returns true
        // if total 100.03 -> Math.floor (100.0) return false

        //Add 50 calPoints to satisfy rule 2 "50 calPoints if the total is a round dollar amount with no cents."
        if (isRoundDollar) {
            calPoints += 50;
        }

        //multiple of quarter
        //convert dollar amount to cents and check if % 25 is 0
        boolean isMultipleOfQuarter = (total * 100) % 25 == 0;
        if (isMultipleOfQuarter) {
            calPoints += 25;
        }
        // add 5 calPoints for every two items on the receipt.
        calPoints += (receipt.getItems().size() / 2) * 5;

        //  Description length multiple of 3 => 0.2 * price
        for (Receipt.Item item : receipt.getItems()) {
            //trim the item description
            String desc = item.getShortDescription().trim();
            if (desc.length() % 3 == 0) {
                double price = Double.parseDouble(item.getPrice());
                price=price * 0.2;
                //roundup the price
                calPoints += Math.ceil(price );
            }
        }

        //  Purchase date is odd
        String purchaseDate = receipt.getPurchaseDate(); // "2022-01-01"
        String[] dateParts = purchaseDate.split("-"); // split string
        int dayOfMonth = Integer.parseInt(dateParts[2]); //get last value

        boolean isOddDay = dayOfMonth % 2 != 0;
        if (isOddDay) {
            calPoints += 6;
        }

        //time between 2 pm to 4 p,
        String[] purchaseTime = receipt.getPurchaseTime().split(":");
        int purchaseHour = Integer.parseInt(purchaseTime[0]);
        int purchaseMinute = Integer.parseInt(purchaseTime[1]);

        int purchaseTimeInMinutes = purchaseHour * 60 + purchaseMinute;
        int startTime = 14 * 60; // 2:00 PM
        int endTime = 16 * 60;   // 4:00 PM (exclusive)

        boolean isBetween2And4PM = purchaseTimeInMinutes >= startTime && purchaseTimeInMinutes < endTime;

        if (isBetween2And4PM) {
            calPoints += 10;
        }

        return calPoints;
    }
}