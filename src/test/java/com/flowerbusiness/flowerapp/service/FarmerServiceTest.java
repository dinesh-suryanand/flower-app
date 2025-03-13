package com.flowerbusiness.flowerapp.service;

import com.flowerbusiness.flowerapp.entity.Farmer;
import com.flowerbusiness.flowerapp.repository.FarmerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FarmerServiceTest {

    @Mock
    private FarmerRepository farmerRepository;

    @InjectMocks
    private FarmerService farmerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFarmer() {
        Farmer farmer = new Farmer(1L, "Ramesh", "9876543210", 0, 10);
        when(farmerRepository.save(farmer)).thenReturn(farmer);

        Farmer savedFarmer = farmerService.addFarmer(farmer);

        assertNotNull(savedFarmer);
        assertEquals("Ramesh", savedFarmer.getFarmerName());
        verify(farmerRepository, times(1)).save(farmer);
    }

    @Test
    void testGetFarmerById() {
        Farmer farmer = new Farmer(1L, "Suresh", "9876543211", 1000, 15);
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));

        Farmer foundFarmer = farmerService.getFarmerById(1L);

        assertNotNull(foundFarmer);
        assertEquals(1L, foundFarmer.getFarmerId());
        verify(farmerRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdatePendingAmount() {
        Farmer farmer = new Farmer(1L, "Mahesh", "9876543212", 5000, 10);
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));

        farmerService.updatePendingAmount(1L, 2000);

        assertEquals(2000, farmer.getTotalPendingAmount());
        verify(farmerRepository, times(1)).save(farmer);
    }
}
