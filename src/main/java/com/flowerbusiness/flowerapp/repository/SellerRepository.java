package com.flowerbusiness.flowerapp.repository;

import com.flowerbusiness.flowerapp.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query("SELECT s FROM Seller s WHERE s.totalPendingAmount > 0")
    List<Seller> findSellersWithPendingAmount();

}
