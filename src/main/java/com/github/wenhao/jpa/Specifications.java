package com.github.wenhao.jpa;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public class Specifications {
    public static <T> Builder<T> and() {
        return new Builder<>(AND);
    }
    public static <T> Builder<T> or() {
        return new Builder<>(OR);
    }
}
