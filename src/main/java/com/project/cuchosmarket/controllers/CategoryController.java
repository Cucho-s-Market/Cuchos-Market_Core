package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping ("/add")
    public DtResponse addCategory(@RequestBody DtCategory newCategory) {
        try {
            categoryService.addCategory(newCategory);
        }
        catch (InvalidCategoryException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return  DtResponse.builder()
                .error(false)
                .message("Categoria agregada correctamente.")
                .build();
    }

    @GetMapping
    public DtResponse getCategories() {
        List<DtCategory> categories = categoryService.getCategories();

        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(categories.size()))
                .data(categories)
                .build();
    }
}
