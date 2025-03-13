package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.entity.Payment;
import com.flowerbusiness.flowerapp.entity.PaymentType;
import com.flowerbusiness.flowerapp.entity.Seller;
import com.flowerbusiness.flowerapp.repository.FarmerRepository;
import com.flowerbusiness.flowerapp.repository.PaymentRepository;
import com.flowerbusiness.flowerapp.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Transactional
    public Payment processPayment(Payment payment) {
        if (payment.getPaymentType() == PaymentType.PAID_TO_FARMER) {
            Farmer farmer = farmerRepository.findById(payment.getFarmer().getFarmerId())
                    .orElseThrow(() -> new RuntimeException("Farmer not found"));
            farmer.setTotalPendingAmount(farmer.getTotalPendingAmount() - payment.getAmount());
            farmerRepository.save(farmer);
        }
        else if (payment.getPaymentType() == PaymentType.RECEIVED_FROM_SELLER) {
            Seller seller = sellerRepository.findById(payment.getSeller().getSellerId())
                    .orElseThrow(() -> new RuntimeException("Seller not found"));
            seller.setTotalPendingAmount(seller.getTotalPendingAmount() - payment.getAmount());
            sellerRepository.save(seller);
        }
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment processDeductionPayment(Long farmerId, double amountPaid) {
        // 1️⃣ Fetch the farmer
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        // 2️⃣ Get the total pending amount
        double totalAmount = farmer.getTotalPendingAmount();

        if (totalAmount <= 0) {
            throw new RuntimeException("No pending amount to settle for this farmer.");
        }

        // 3️⃣ Apply deduction (10% or 15%) only to the total pending amount
        double deduction = farmer.getDeductionPercentage() > 0 ? (totalAmount * farmer.getDeductionPercentage()) / 100 : 0;
        double finalAmount = totalAmount - deduction;   // This is the amount eligible for payment

        // 4️⃣ Ensure amountPaid is not more than finalAmount
        if (amountPaid > finalAmount) {
            throw new IllegalArgumentException("Amount paid cannot be greater than the final payable amount after deduction.");
        }

        // 5️⃣ Create a payment record
        Payment payment = new Payment();
        payment.setFarmer(farmer);
        payment.setAmount(amountPaid);
        payment.setPaymentType(PaymentType.PAID_TO_FARMER);
        payment.setDate(LocalDate.now());

        // 6️⃣ Reduce the pending amount only by what is paid, not setting it to zero
        farmer.setTotalPendingAmount(totalAmount - amountPaid);

        // 7️⃣ Save changes
        farmerRepository.save(farmer);
        return paymentRepository.save(payment);
    }


}
