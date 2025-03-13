package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.entity.Payment;
import com.flowerbusiness.flowerapp.entity.PaymentType;
import com.flowerbusiness.flowerapp.entity.Seller;
import com.flowerbusiness.flowerapp.repository.FarmerRepository;
import com.flowerbusiness.flowerapp.repository.PaymentRepository;
import com.flowerbusiness.flowerapp.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test Payment to Farmer
    @Test
    void testProcessPaymentToFarmer() {
        Farmer farmer = new Farmer(1L, "Ramesh", "9876543210", 10000.0, 10.0);
        Payment payment = new Payment(1L, LocalDate.now(), 5000.0, PaymentType.PAID_TO_FARMER, farmer, null);

        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment processedPayment = paymentService.processPayment(payment);

        assertNotNull(processedPayment);
        assertEquals(5000.0, processedPayment.getAmount());
        assertEquals(5000.0, farmer.getTotalPendingAmount()); // 10000 - 5000
        verify(farmerRepository, times(1)).save(farmer);
        verify(paymentRepository, times(1)).save(payment);
    }

    // ✅ Test Payment Received from Seller
    @Test
    void testProcessPaymentFromSeller() {
        Seller seller = new Seller(1L, "Rahul", "9876543211", 8000.0);
        Payment payment = new Payment(1L, LocalDate.now(), 3000.0, PaymentType.RECEIVED_FROM_SELLER, null, seller);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment processedPayment = paymentService.processPayment(payment);

        assertNotNull(processedPayment);
        assertEquals(3000.0, processedPayment.getAmount());
        assertEquals(5000.0, seller.getTotalPendingAmount()); // 8000 - 3000
        verify(sellerRepository, times(1)).save(seller);
        verify(paymentRepository, times(1)).save(payment);
    }

    // ✅ Test End-of-Month Deduction Payment
    @Test
    void testProcessDeductionPayment() {
        // ✅ Ensure farmer has pending amount > 0
        Farmer farmer = new Farmer(1L, "Suresh", "9876543212", 20000.0, 10.0);
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));

        // ✅ Mock paymentRepository.save() to return a valid Payment
        Payment mockPayment = new Payment(1L, LocalDate.now(), 15000.0, PaymentType.PAID_TO_FARMER, farmer, null);
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        // ✅ Execute test
        Payment payment = paymentService.processDeductionPayment(1L, 15000.0);

        // ✅ Assertions
        assertNotNull(payment, "Payment should not be null");
        assertEquals(15000.0, payment.getAmount());
        assertTrue(farmer.getTotalPendingAmount() >= 0, "Farmer's pending amount should be >= 0");

        // ✅ Verify repository interactions
        verify(farmerRepository, times(1)).save(farmer);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }


    // ❌ Test Deduction Payment When No Pending Amount
    @Test
    void testProcessDeductionPayment_NoPendingAmount() {
        Farmer farmer = new Farmer(1L, "Raj", "9876543213", 0.0, 10.0);
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.processDeductionPayment(1L, 5000.0);
        });

        assertEquals("No pending amount to settle for this farmer.", exception.getMessage());
        verify(farmerRepository, times(1)).findById(1L);
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    // ❌ Test Overpayment Beyond Deducted Amount
    @Test
    void testProcessDeductionPayment_Overpayment() {
        Farmer farmer = new Farmer(1L, "Mahesh", "9876543214", 20000.0, 10.0);
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processDeductionPayment(1L, 19000.0); // Trying to pay more than allowed after deduction
        });

        assertEquals("Amount paid cannot be greater than the final payable amount after deduction.", exception.getMessage());
        verify(farmerRepository, times(1)).findById(1L);
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }
}
