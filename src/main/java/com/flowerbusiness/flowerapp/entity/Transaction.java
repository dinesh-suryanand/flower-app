package com.flowerbusiness.flowerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING) // Ensures safe storage
    @Column(nullable = false)
    private TransactionType transactionType; // "Purchase from Farmer" or "Sale to Seller"

    @Column(nullable = false)
    private Integer quantity; // e.g., 10 kg

    @Column(nullable = false)
    private Double pricePerUnit; // qe.g., ₹50/unit

    @Column(nullable = false)
    private Double totalAmount; // quantity * pricePerUnit

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = true) // Optional if it's a sale
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = true) // Optional if it's a purchase
    private Seller seller;

//TODO movie to service layer
//    @PrePersist
//    public void updatePendingAmounts() {
//        if (transactionType == TransactionType.FROM_FARMER && farmer != null) {
//            farmer.setTotalPendingAmount(farmer.getTotalPendingAmount() + totalAmount);
//        } else if (transactionType == TransactionType.TO_SELLER && seller != null) {
//            seller.setTotalPendingAmount(seller.getTotalPendingAmount() + totalAmount);
//        }
//    }

}
