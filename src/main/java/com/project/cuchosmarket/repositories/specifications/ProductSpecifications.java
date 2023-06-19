package com.project.cuchosmarket.repositories.specifications;

import com.project.cuchosmarket.models.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> filterByAttributes(Long branchId, String name, String brand, String orderBy,
                                                            String orderDirection, Long categoryId) {
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
            if (categoryId != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }
            if (branchId != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Stock> stockRoot = subquery.from(Stock.class);
                Join<Stock, StockId> stockIdJoin = stockRoot.join("id");
                Join<StockId, Branch> branchJoin = stockIdJoin.join("branch");
                subquery.select(stockIdJoin.get("product").get("name"))
                        .where(criteriaBuilder.equal(branchJoin.get("id"), branchId));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.in(root.get("name")).value(subquery));
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
