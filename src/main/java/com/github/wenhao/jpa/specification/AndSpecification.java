package com.github.wenhao.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AndSpecification<T> implements Specification<T>, Serializable {

    private Specification specification;

    public AndSpecification(Specification specification) {
        this.specification = specification;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return specification.toPredicate(root, query, cb);
    }
}
