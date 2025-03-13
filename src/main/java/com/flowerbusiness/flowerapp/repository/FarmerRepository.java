package com.flowerbusiness.flowerapp.repository;

import com.flowerbusiness.flowerapp.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    @Query("SELECT f FROM Farmer f WHERE f.totalPendingAmount > 0")
    List<Farmer> findFarmersWithPendingAmount();
}
