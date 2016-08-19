package com.github.wenhao.jpa.specification;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        this.patterns = new HashSet<>(Arrays.asList(patterns)).stream().toArray(String[]::new);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (patterns.length == 1) {
            return criteriaBuilder.like(root.get(property), patterns[0]);
        }
        List<Predicate> predicates = Arrays.asList(Arrays.copyOfRange(patterns, 0, patterns.length - 1)).stream()
                .map(value -> criteriaBuilder.like(root.get(property), value))
                .collect(toList());
        predicates.add(criteriaBuilder.like(root.get(property), patterns[patterns.length - 1]));
        return criteriaBuilder.or(predicates.stream().toArray(Predicate[]::new));
    }
}
