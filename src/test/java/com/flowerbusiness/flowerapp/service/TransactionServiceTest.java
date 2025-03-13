package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.entity.Seller;
import com.flowerbusiness.flowerapp.entity.Transaction;
import com.flowerbusiness.flowerapp.entity.TransactionType;
import com.flowerbusiness.flowerapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FarmerService farmerService;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTransactionFromFarmer() {
        Farmer farmer = new Farmer(1L, "Suresh", "9876543210", 0, 10);
        Transaction transaction = new Transaction(1L, null, TransactionType.FROM_FARMER, 10, 50.0, 500.0, farmer, null);

        when(farmerService.getFarmerById(1L)).thenReturn(farmer);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertNotNull(savedTransaction);
        assertEquals(500.0, savedTransaction.getTotalAmount());
        verify(farmerService, times(1)).updatePendingAmount(1L, 500.0);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testSaveTransactionToSeller() {
        // ✅ Mock seller object
        Seller seller = new Seller(1L, "Ramesh", "9876543211", 0.0);
        Transaction transaction = new Transaction(2L, null, TransactionType.TO_SELLER, 5, 60.0, 300.0, null, seller);

        // ✅ Mock sellerService
        when(sellerService.getSellerById(1L)).thenReturn(seller);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // ✅ Execute test
        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        // ✅ Assertions
        assertNotNull(savedTransaction);
        assertEquals(300.0, savedTransaction.getTotalAmount());

        // ✅ Verify correct interactions
        verify(sellerService, times(1)).getSellerById(1L);
        verify(sellerService, times(1)).updatePendingAmount(1L, 300.0);  // ✅ Now this should be called
        verify(transactionRepository, times(1)).save(transaction);
    }

}
