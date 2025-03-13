package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    public Farmer addFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Farmer getFarmerById(Long farmerId) {
        return farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
    }

    public void updatePendingAmount(Long farmerId, double amountChange) {
        Farmer farmer = getFarmerById(farmerId);
        double updatedAmount = farmer.getTotalPendingAmount() + amountChange;
        farmer.setTotalPendingAmount(updatedAmount);  //  negative balance should also be included since it is transaction.
        farmerRepository.save(farmer);
    }

}
