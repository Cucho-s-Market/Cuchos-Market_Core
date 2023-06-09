package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtPromotion;
import com.project.cuchosmarket.exceptions.InvalidPromotionException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.exceptions.PromotionNotExistException;
import com.project.cuchosmarket.models.Discount;
import com.project.cuchosmarket.models.NxM;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.models.Promotion;
import com.project.cuchosmarket.repositories.ProductRepository;
import com.project.cuchosmarket.repositories.PromotionRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    private List<Product> validatePromotion(DtPromotion dtPromotion) throws InvalidPromotionException, ProductNotExistException {
        if (StringUtils.isBlank(dtPromotion.getName()) || dtPromotion.getName().length() > 50) throw new InvalidPromotionException("Datos invalidos: Nombre invalido.");
        if (dtPromotion.getStartDate() == null || dtPromotion.getEndDate() == null) throw new InvalidPromotionException("Datos invalidos: Periodo de promocion invalido.");
        if (dtPromotion.getProducts() != null) {
            List<Product> promotionProducts = new ArrayList<>();
            for (DtProduct dtProduct : dtPromotion.getProducts()) {
                promotionProducts.add(productRepository.findById(dtProduct.getName()).orElseThrow(() -> new ProductNotExistException(dtProduct.getName())));
            }
            return promotionProducts;
        }
        return null;
    }

    private void validateDiscount(DtPromotion dtPromotion) throws InvalidPromotionException {
        if (dtPromotion.getPercentage() < 0 || dtPromotion.getPercentage() > 100 ) throw new InvalidPromotionException();
    }

    private void validateNxM(DtPromotion dtPromotion) throws InvalidPromotionException {
        if (dtPromotion.getN() <= 0 || dtPromotion.getM() <= 0) throw new InvalidPromotionException();
    }

    public void addPromotion(DtPromotion dtPromotion) throws InvalidPromotionException, ProductNotExistException {
        List<Product> products = validatePromotion(dtPromotion);

        if (dtPromotion.getPromotionType().equalsIgnoreCase("discount")) {
            validateDiscount(dtPromotion);
            Discount discount = new Discount(dtPromotion.getName(), dtPromotion.getStartDate(), dtPromotion.getEndDate(),
                    dtPromotion.getImage(), products, dtPromotion.getPercentage());
            promotionRepository.save(discount);

        } else if (dtPromotion.getPromotionType().equalsIgnoreCase("nxm")) {
            validateNxM(dtPromotion);
            NxM nxM = new NxM(dtPromotion.getName(), dtPromotion.getStartDate(), dtPromotion.getEndDate(), dtPromotion.getImage(),
                    products, dtPromotion.getN(), dtPromotion.getM());
            promotionRepository.save(nxM);

        } else throw new InvalidPromotionException("El sistema no soporta ese tipo de promocion.");
    }

    public void updatePromotion(DtPromotion dtPromotion) throws InvalidPromotionException, PromotionNotExistException, ProductNotExistException {
        List<Product> products = validatePromotion(dtPromotion);
        Promotion promotion = promotionRepository.findById(dtPromotion.getId()).orElseThrow(PromotionNotExistException::new);

        promotion.setName(dtPromotion.getName());
        promotion.setStartDate(dtPromotion.getStartDate());
        promotion.setEndDate(dtPromotion.getEndDate());
        promotion.setImage(dtPromotion.getImage());
        promotion.setProducts(products);

        if (dtPromotion.getPromotionType().equalsIgnoreCase("discount")) {
            validateDiscount(dtPromotion);

            Discount discount = (Discount) promotion;
            discount.setPercentage(dtPromotion.getPercentage());
            promotionRepository.save(discount);
        } else if (dtPromotion.getPromotionType().equalsIgnoreCase("nxm")) {
            validateNxM(dtPromotion);

            NxM nxM = (NxM) promotion;
            nxM.setN(dtPromotion.getN());
            nxM.setM(dtPromotion.getM());
            promotionRepository.save(nxM);
        } else throw new InvalidPromotionException("El sistema no soporta ese tipo de promocion.");


    }

    public List<Promotion> getPromotions(boolean includeExpired) {     //TODO Paginable?
        return promotionRepository.findPromotions(includeExpired);   //TODO Capaz una funcion de listar promociones en general como objetos?
                                                //TODO Y otra funcion para conseguir los productos de esa promocion o agregar filtro a listar Productos?
    }
}
