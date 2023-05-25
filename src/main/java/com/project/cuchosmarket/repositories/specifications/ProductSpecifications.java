package com.project.cuchosmarket.repositories.specifications;

import com.project.cuchosmarket.models.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> filterByAttributes(String name, String brand, String orderBy, String orderDirection) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }
            if (brand != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")),
                        brand.toLowerCase()));
            }
            if (orderBy != null) {
                query.orderBy(criteriaBuilder.asc(root.get(orderBy)));
                if (orderDirection == null || orderDirection.equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(orderBy)));
                }
            }
            return predicate;
        };
    }
}
