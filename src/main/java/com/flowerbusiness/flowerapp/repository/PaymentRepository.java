package com.flowerbusiness.flowerapp.repository;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.entity.Payment;
import com.flowerbusiness.flowerapp.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByFarmer(Farmer farmer);
    List<Payment> findBySeller(Seller seller);

}
