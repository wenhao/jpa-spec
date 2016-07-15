package com.github.wenhao.jpa.specification;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class GtSpification<T> implements Specification<T>, Serializable {
    private final String property;
    private final Number number;

    public GtSpification(String property, Number number) {
        this.property = property;
        this.number = number;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.gt(root.get(property), number);
    }
}
