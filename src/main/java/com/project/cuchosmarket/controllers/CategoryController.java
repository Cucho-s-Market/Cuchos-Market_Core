package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categorias")
public class CategoryController {
    private final CategoryService categoryService;


}
