package com.github.wenhao.jpa.specification;

import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

abstract class AbstractSpecification<T> implements Specification<T>, Serializable {
    public String getProperty(String property) {
        if (property.contains(".")) {
            return substringAfter(property, ".");
        }
        return property;
    }

    public From getRoot(String property, Root<T> root) {
        if (property.contains(".")) {
            String joinProperty = substringBefore(property, ".");
            return root.join(joinProperty, JoinType.INNER);
        }
        return root;
    }
}
