package com.project.cuchosmarket.repositories.specifications;

import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.models.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {
    public static Specification<Order> filterByAttributes(List<Long> orderIdsFromBranch, OrderStatus orderStatus, LocalDate startDate,
                                                          LocalDate endDate, String orderDirection) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!orderIdsFromBranch.isEmpty()) {
                predicates.add(root.get("id").in(orderIdsFromBranch));
            }

            if (orderStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("creationDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), startDate));
            } else if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), endDate));
            }

            if (orderDirection == null || orderDirection.equalsIgnoreCase("desc")) {
                query.orderBy(criteriaBuilder.desc(root.get("creationDate")));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get("creationDate")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
