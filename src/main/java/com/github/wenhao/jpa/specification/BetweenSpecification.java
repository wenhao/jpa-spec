package com.github.wenhao.jpa.specification;


import com.google.common.collect.Range;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class BetweenSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Range range;

    public BetweenSpecification(String property, Range range) {
        this.property = property;
        this.range = range;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        return cb.between(from.get(field), range.lowerEndpoint(), range.upperEndpoint());
    }
}
