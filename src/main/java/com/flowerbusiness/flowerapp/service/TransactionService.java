package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.entity.Seller;
import com.flowerbusiness.flowerapp.entity.Transaction;
import com.flowerbusiness.flowerapp.entity.TransactionType;
import com.flowerbusiness.flowerapp.repository.SellerRepository;
import com.flowerbusiness.flowerapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;



    @Autowired
    private SellerService sellerService;

    @Autowired
    private FarmerService farmerService;

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        // ✅ Ensure transaction has required entities
        if (transaction.getTransactionType() == TransactionType.FROM_FARMER && transaction.getFarmer() == null) {
            throw new IllegalArgumentException("Farmer must be provided for FROM_FARMER transactions");
        }
        if (transaction.getTransactionType() == TransactionType.TO_SELLER && transaction.getSeller() == null) {
            throw new IllegalArgumentException("Seller must be provided for TO_SELLER transactions");
        }

        // ✅ Process Transactions
        if (transaction.getTransactionType() == TransactionType.FROM_FARMER) {
            Farmer farmer = farmerService.getFarmerById(transaction.getFarmer().getFarmerId()); // Using FarmerService
            farmerService.updatePendingAmount(farmer.getFarmerId(), farmer.getTotalPendingAmount() + transaction.getTotalAmount());
        }
        else if (transaction.getTransactionType() == TransactionType.TO_SELLER) {
            Seller seller = sellerService.getSellerById(transaction.getSeller().getSellerId());
            double updatedPendingAmount = seller.getTotalPendingAmount() + transaction.getTotalAmount();
            sellerService.updatePendingAmount(seller.getSellerId(), updatedPendingAmount);
        }


        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForFarmer(Long farmerId) {
        return transactionRepository.findByFarmer(farmerService.getFarmerById(farmerId));
    }



    public List<Transaction> getTransactionsForSeller(Long sellerId) {
        return transactionRepository.findBySeller(sellerService.getSellerById(sellerId));
    }


}
