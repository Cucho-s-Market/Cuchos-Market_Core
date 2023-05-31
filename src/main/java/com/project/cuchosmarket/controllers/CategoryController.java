package com.project.cuchosmarket.controllers;



import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/add-category")
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
}
