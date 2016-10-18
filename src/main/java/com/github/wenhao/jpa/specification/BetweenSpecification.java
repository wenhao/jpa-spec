package com.github.wenhao.jpa.specification;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;


public class BetweenSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final Range range;

    public BetweenSpecification(String property, Range range) {
        this.property = property;
        this.range = range;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.between(root.get(property), range.getLowerBound(), range.getUpperBound());
    }
}
