package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Seller;
import com.flowerbusiness.flowerapp.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public Seller addSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
    }

    public void updatePendingAmount(Long sellerId, double amountChange) {
        Seller seller = getSellerById(sellerId);
        seller.setTotalPendingAmount(seller.getTotalPendingAmount() + amountChange);
        sellerRepository.save(seller);
    }

}
