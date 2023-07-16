package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtPromotion;
import com.project.cuchosmarket.exceptions.InvalidPromotionException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.exceptions.PromotionNotExistException;
import com.project.cuchosmarket.models.NxM;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.models.Promotion;
import com.project.cuchosmarket.repositories.ProductRepository;
import com.project.cuchosmarket.repositories.PromotionRepository;
import com.project.cuchosmarket.services.PromotionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionServiceTest {
    @MockBean
    private PromotionRepository promotionRepository;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private PromotionService promotionService;

    @DisplayName("Agregar promocion")
    @Test
    public void testAddPromotion() throws InvalidPromotionException, ProductNotExistException {
        DtPromotion dtPromotion = new DtPromotion(1l,"promo1", LocalDate.now(),LocalDate.now().plusDays(15),
                "imag1", List.of(new DtProduct()),0,2,1,"nxm");

        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        when(promotionRepository.findPromotionsByProduct(any())).thenReturn(new ArrayList<>());

        promotionService.addPromotion(dtPromotion);
        verify(promotionRepository, times(1)).save(any());
    }

    @DisplayName("Editar promocion")
    @Test
    public void testUpdatePromotion() throws InvalidPromotionException, ProductNotExistException, PromotionNotExistException {
        DtPromotion dtPromotion = new DtPromotion(1l,"promo1", LocalDate.now(),LocalDate.now().plusDays(15),
                "imag1", null,0,2,1,"nxm");
        when(promotionRepository.findById(dtPromotion.getId())).thenReturn(Optional.of(new NxM()));
        promotionService.updatePromotion(dtPromotion);
        verify(promotionRepository, times(1)).save(any());
    }

    @DisplayName("Listar promociones")
    @Test
    public void testGetPromotions() {
      boolean includeExpired = false;
      List<Promotion> promotion = new ArrayList<>();

      when(promotionRepository.findPromotions(includeExpired)).thenReturn(promotion);
      List<Promotion> salida = promotionService.getPromotions(includeExpired);
      assertNotNull(salida);
    }
}
