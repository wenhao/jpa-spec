package com.github.wenhao.jpa.specification;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class EqualSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final Object[] values;

    public EqualSpecification(String property, Object... values) {
        this.property = property;
        this.values = new HashSet<>(Arrays.asList(values)).toArray();
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (values.length == 1) {
            return criteriaBuilder.equal(root.get(property), values[0]);
        }
        Arrays.asList(Arrays.copyOfRange(values, 0, values.length - 2)).forEach(value -> {
            if (Objects.isNull(value)) {
                criteriaBuilder.or(criteriaBuilder.isNull(root.get(property)));
            } else {
                criteriaBuilder.or(criteriaBuilder.equal(root.get(property), value));
            }
        });
        return criteriaBuilder.or(criteriaBuilder.equal(root.get(property), values[values.length - 1]));
    }
}

