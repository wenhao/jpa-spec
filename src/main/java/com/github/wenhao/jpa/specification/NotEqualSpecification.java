package com.github.wenhao.jpa.specification;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class NotEqualSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final Object[] values;

    public NotEqualSpecification(String property, Object... values) {
        this.property = property;
        this.values = new HashSet<>(Arrays.asList(values)).toArray();
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (values.length == 1) {
            if (isNull(values[0])) {
                return criteriaBuilder.isNotNull(root.get(property));
            }
            return criteriaBuilder.notEqual(root.get(property), values[0]);
        }
        Arrays.asList(Arrays.copyOfRange(values, 0, values.length - 2)).forEach(value -> {
            if (Objects.isNull(value)) {
                criteriaBuilder.or(criteriaBuilder.isNotNull(root.get(property)));
            } else {
                criteriaBuilder.or(criteriaBuilder.notEqual(root.get(property), value));
            }
        });
        return criteriaBuilder.or(criteriaBuilder.notEqual(root.get(property), values[values.length - 1]));
    }
}
