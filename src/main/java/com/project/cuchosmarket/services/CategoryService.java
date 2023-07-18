package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.repositories.CategoryRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private void validateCategory(DtCategory dtCategory) throws InvalidCategoryException {
        if(StringUtils.isBlank(dtCategory.getName()) || dtCategory.getName().length() > 50) throw new InvalidCategoryException();
        if(categoryRepository.existsByName(dtCategory.getName())) throw new InvalidCategoryException("La categoria ya existe.");
        if(StringUtils.isBlank(dtCategory.getDescription()) || dtCategory.getDescription().length() > 255
                || dtCategory.getDescription().length() < 5) throw new InvalidCategoryException();
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
                newCategory.setCategoryParent(categoryParent.get().getId());

                categoryRepository.save(categoryParent.get());
            }
            else throw new InvalidCategoryException("La categoria padre no existe.");
        }
        else {
            newCategory.setCategoryParent(0L);
        }
        categoryRepository.save(newCategory);
    }

    public List<DtCategory> getCategories() {
        List<DtCategory> dtCategories = new ArrayList<>();
        List<Category> categories = categoryRepository.findByOrderByCategoryParentAsc();

        categories.forEach(
                category -> dtCategories.add(new DtCategory(category.getId(), category.getName(), category.getDescription(), category.getImage(), category.getCategoryParent()))
        );

        return  dtCategories;
    }
}
