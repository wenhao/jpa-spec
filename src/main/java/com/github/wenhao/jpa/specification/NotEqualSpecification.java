package com.github.wenhao.jpa.specification;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.Arrays;

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
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (values.length == 1) {
            return getPredicate(root, cb, values[0]);
        }
        Predicate[] predicates = Arrays.stream(values)
            .map(value -> getPredicate(root, cb, value))
            .toArray(Predicate[]::new);
        return cb.or(predicates);
    }

    private Predicate getPredicate(Root<T> root, CriteriaBuilder cb, Object value) {
        return isNull(value) ? cb.isNotNull(root.get(property)) : cb.notEqual(root.get(property), value);
    }
}

