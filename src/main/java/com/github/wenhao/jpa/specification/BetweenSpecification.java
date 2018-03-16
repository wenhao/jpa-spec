package com.github.wenhao.jpa.specification;

import org.springframework.data.domain.Range;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class BetweenSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Range<? extends Object> range;

    public BetweenSpecification(String property, Range<? extends Object> range) {
        this.property = property;
        this.range = range;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        Comparable<Object> lower = (Comparable<Object>) range.getLowerBound().getValue().get();
        Comparable<Object> upper = (Comparable<Object>) range.getUpperBound().getValue().get();
        return cb.between(from.get(field), lower, upper);
    }
}
