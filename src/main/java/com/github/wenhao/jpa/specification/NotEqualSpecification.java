package com.github.wenhao.jpa.specification;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import static com.github.wenhao.jpa.util.ArrayUtils.removeDuplicate;

public class NotEqualSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final Object[] values;

    public NotEqualSpecification(String property, Object... values) {
        this.property = property;
        this.values = removeDuplicate(values);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (values.length == 1) {
            if (isNull(values[0])) {
                return criteriaBuilder.isNotNull(root.get(property));
            }
            return criteriaBuilder.notEqual(root.get(property), values[0]);
        }

        List<Predicate> predicates = new ArrayList<>();
        for (int i = 0; i < values.length - 1; i++) {
            if (Objects.isNull(values[i])) {
                predicates.add(criteriaBuilder.isNotNull(root.get(property)));
            } else {
                predicates.add(criteriaBuilder.notEqual(root.get(property), values[i]));
            }
        }
        predicates.add(criteriaBuilder.notEqual(root.get(property), values[values.length - 1]));
        return criteriaBuilder.or(predicates.stream().toArray(Predicate[]::new));
    }
}

