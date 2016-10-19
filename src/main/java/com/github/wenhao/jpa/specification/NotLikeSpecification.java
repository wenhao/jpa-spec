package com.github.wenhao.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotLikeSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final String[] patterns;

    public NotLikeSpecification(String property, String... patterns) {
        this.property = property;
        this.patterns = patterns;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (patterns.length == 1) {
            return cb.like(root.get(property), patterns[0]).not();
        }
        Predicate[] predicates = Arrays.stream(patterns)
            .map(value -> cb.like(root.get(property), value).not())
            .toArray(Predicate[]::new);
        return cb.or(predicates);
    }
}
