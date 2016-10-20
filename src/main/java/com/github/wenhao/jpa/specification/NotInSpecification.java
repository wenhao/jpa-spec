package com.github.wenhao.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotInSpecification<T> implements Specification<T> {
    private String property;
    private Object[] values;

    public NotInSpecification(String property, Object[] values) {
        this.property = property;
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<Object> field = root.get(property);
        return field.in(values).not();
    }
}
