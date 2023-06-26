package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtPromotion;
import com.project.cuchosmarket.exceptions.InvalidPromotionException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.exceptions.PromotionNotExistException;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.ProductRepository;
import com.project.cuchosmarket.repositories.PromotionRepository;
import com.project.cuchosmarket.services.PromotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionServiceTest {
    @MockBean
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionService promotionService;
    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testAddPromotion() throws InvalidPromotionException, ProductNotExistException {

        Category category = new Category(2l,"merienda", "merienda","imagMerienda",1l);

        Product product = new Product("codeAlfa","alfajor","dulce de leche", LocalDate.now(),35,"brandAlfa", category,null);
        List<Product> productos = Arrays.asList();
        productos.add(product);
        List<DtProduct> dtProducts = Arrays.asList(
                new DtProduct("codeAlfa","alfajor","dulce de leche", 35,LocalDate.now(),"brandAlfa",2l,null)
        );
        DtPromotion dtPromotion = new DtPromotion(1l,"promo1", LocalDate.now(),LocalDate.now().plusDays(15),"imag1", dtProducts,0,2,1,"nxm");

        when(productRepository.findById(dtProducts.get(0).getName())).thenReturn(Optional.of(product));

        promotionService.addPromotion(dtPromotion);
    }

    @Test
    public void testUpdatePromotion() throws InvalidPromotionException, ProductNotExistException {
        DtPromotion dtPromotion = new DtPromotion(1l,"promo1", LocalDate.now(),LocalDate.now().plusDays(15),"imag1", null,15,2,1,"nxm");
        Promotion promotion = null;
        when(promotionRepository.findById(dtPromotion.getId())).thenReturn(Optional.ofNullable(promotion));
        promotionService.addPromotion(dtPromotion);
    }

    @Test
    public void testGetPromotions() {
      boolean includeExpired = false;
      List<Promotion> promotion = null;

      when(promotionRepository.findPromotions(includeExpired)).thenReturn(promotion);
      List<Promotion> salida = promotionService.getPromotions(includeExpired);
    }


}
