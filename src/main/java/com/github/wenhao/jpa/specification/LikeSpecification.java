package com.github.wenhao.jpa.specification;

import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LikeSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final String[] patterns;

    public LikeSpecification(String property, String... patterns) {
        this.property = property;
        this.patterns = patterns;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From<? extends Object, ? extends Object> from = getRoot(property, root);
        String field = getProperty(property);
        if (patterns.length == 1) {
            return cb.like(from.get(field), patterns[0]);
        }
        Predicate[] predicates = Arrays.stream(patterns)
            .map(value -> cb.like(from.get(field), value))
            .toArray(Predicate[]::new);
        return cb.or(predicates);
    }
}
