package com.github.wenhao.jpa.specification;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class NotEqualSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final Object object;

    public NotEqualSpecification(String property, Object object) {
        this.property = property;
        this.object = object;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(root.get(property), object);
    }
}
