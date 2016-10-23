package com.github.wenhao.jpa.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GeSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Number number;

    public GeSpecification(String property, Number number) {
        this.property = property;
        this.number = number;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        return cb.ge(from.get(field), number);
    }
}
