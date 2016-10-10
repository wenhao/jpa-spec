package com.github.wenhao.jpa.specification;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.Arrays;

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
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (values.length == 1) {
            return getPredicate(root, criteriaBuilder, values[0]);
        }
        Predicate[] predicates = Arrays.stream(values)
            .map(value -> getPredicate(root, criteriaBuilder, value))
            .toArray(Predicate[]::new);
        return criteriaBuilder.or(predicates);
    }

    private Predicate getPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Object value) {
        return isNull(value) ? criteriaBuilder.isNull(root.get(property)) : criteriaBuilder.equal(root.get(property), value);
    }
}

