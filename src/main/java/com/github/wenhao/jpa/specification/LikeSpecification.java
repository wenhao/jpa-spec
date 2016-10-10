package com.github.wenhao.jpa.specification;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class LikeSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final String[] patterns;

    public LikeSpecification(String property, String... patterns) {
        this.property = property;
        this.patterns = patterns;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (patterns.length == 1) {
            return criteriaBuilder.like(root.get(property), patterns[0]);
        }
        Predicate[] predicates = Arrays.stream(patterns)
            .map(value -> criteriaBuilder.like(root.get(property), value))
            .toArray(Predicate[]::new);
        return criteriaBuilder.or(predicates);
    }
}
