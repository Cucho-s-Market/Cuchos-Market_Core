package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtPromotion;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.InvalidPromotionException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.exceptions.PromotionNotExistException;
import com.project.cuchosmarket.models.Promotion;
import com.project.cuchosmarket.services.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/promotions")
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public DtResponse addPromotion(@RequestBody DtPromotion dtPromotion) {
        try {
            promotionService.addPromotion(dtPromotion);
        } catch (InvalidPromotionException | ProductNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Promocion agregada correctamente")
                .build();
    }

    @PutMapping
    public DtResponse updatePromotion(@RequestBody DtPromotion dtPromotion) {
        try {
            promotionService.updatePromotion(dtPromotion);
        } catch (InvalidPromotionException | ProductNotExistException | PromotionNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Promocion actualizada correctamente")
                .build();
    }

    @GetMapping
    public DtResponse getPromotion(@RequestParam(value = "includeExpired", required = false, defaultValue = "false") boolean includeExpired) {
        List<Promotion> promotions = promotionService.getPromotions(includeExpired);
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(promotions.size()))
                .data(promotions)
                .build();
    }
}
