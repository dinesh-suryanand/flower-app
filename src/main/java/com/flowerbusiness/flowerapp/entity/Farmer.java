package com.flowerbusiness.flowerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "farmers")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long farmerId;

    @Column(nullable = false)
    private String farmerName;

    private String whatsAppNumber;

    @Column(nullable = false)
    private double totalPendingAmount;  // Amount pending after deductions

    @Column(nullable = false)
    private double deductionPercentage;  // 10% or 15%

//    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Transaction> transactions;
//
//    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Payment> payments = new ArrayList<>();
}
