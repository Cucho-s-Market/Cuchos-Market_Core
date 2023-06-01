package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private void validateCategory(DtCategory dtCategory) throws InvalidCategoryException {
        if(dtCategory.getName() == null) throw new InvalidCategoryException();
        if(categoryRepository.existsByName(dtCategory.getName())) throw  new InvalidCategoryException("La categoria ya existe.");
    }

    public void addCategory(DtCategory dtCategory) throws InvalidCategoryException {
        Optional<Category> categoryParent;

        //Validations
        validateCategory(dtCategory);

        //New category creation
        Category newCategory = new Category(
                dtCategory.getName(),
                dtCategory.getDescription(),
                dtCategory.getImage()
        );

        if(dtCategory.getCategoryParent() != null) {
            categoryParent = categoryRepository.findById(dtCategory.getCategoryParent());

            if(categoryParent.isPresent()) {
                categoryParent.get().addSubcategory(newCategory);
                newCategory.setCategoryParent(categoryParent.get());

                categoryRepository.save(categoryParent.get());
            }
            else throw new InvalidCategoryException("La categoria padre no existe.");
        }
        categoryRepository.save(newCategory);
    }
}
