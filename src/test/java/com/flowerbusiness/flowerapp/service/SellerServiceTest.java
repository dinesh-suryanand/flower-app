package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Seller;
import com.flowerbusiness.flowerapp.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSeller() {
        Seller seller = new Seller(1L, "John", "9876543210", 0.0);
        when(sellerRepository.save(seller)).thenReturn(seller);

        Seller savedSeller = sellerService.addSeller(seller);

        assertNotNull(savedSeller);
        assertEquals("John", savedSeller.getSellerName());
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    void testGetSellerById() {
        Seller seller = new Seller(1L, "David", "9876543211", 1000.0);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Seller foundSeller = sellerService.getSellerById(1L);

        assertNotNull(foundSeller);
        assertEquals(1L, foundSeller.getSellerId());
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdatePendingAmount() {
        Seller seller = new Seller(1L, "Alice", "9876543212", 5000.0);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        sellerService.updatePendingAmount(1L, 2000.0);

        assertEquals(2000.0, seller.getTotalPendingAmount());
        verify(sellerRepository, times(1)).save(seller);
    }
}
