package com.github.wenhao.jpa.specification;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class LikeSpecification<T> implements Specification<T>, Serializable {
    private final String property;
    private final String pattern;

    public LikeSpecification(String property, String pattern) {
        this.property = property;
        this.pattern = pattern;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get(property), pattern);
    }
}
