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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double amount; // Amount paid or received

    @Enumerated(EnumType.STRING) // Ensures safe storage
    @Column(nullable = false)
    private PaymentType paymentType; // "Paid to Farmer" or "Received from Seller"

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = true) // Optional if it's a seller payment
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = true) // Optional if it's a farmer payment
    private Seller seller;

// TODO move this logic to service layer
//    @PrePersist
//    public void updatePendingAmounts() {
//        if (paymentType == PaymentType.PAID_TO_FARMER && farmer != null) {
//            farmer.setTotalPendingAmount(farmer.getTotalPendingAmount() - amount);
//        } else if (paymentType == PaymentType.RECEIVED_FROM_SELLER && seller != null) {
//            seller.setTotalPendingAmount(seller.getTotalPendingAmount() - amount);
//        }
//    }
}
