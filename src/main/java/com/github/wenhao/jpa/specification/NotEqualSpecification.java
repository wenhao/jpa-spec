package com.github.wenhao.jpa.specification;

import static java.util.Objects.isNull;

import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotEqualSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Object[] values;

    public NotEqualSpecification(String property, Object... values) {
        this.property = property;
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        if (isNull(values)) {
            return cb.isNotNull(from.get(field));
        }
        if (values.length == 1) {
            return getPredicate(from, cb, values[0], field);
        }
        Predicate[] predicates = Arrays.stream(values)
            .map(value -> getPredicate(root, cb, value, field))
            .toArray(Predicate[]::new);
        return cb.or(predicates);
    }

    private Predicate getPredicate(From root, CriteriaBuilder cb, Object value, String field) {
        return isNull(value) ? cb.isNotNull(root.get(field)) : cb.notEqual(root.get(field), value);
    }
}

