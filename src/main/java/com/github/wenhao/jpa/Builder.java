package com.github.wenhao.jpa;

import com.github.wenhao.jpa.specification.BetweenSpecification;
import com.github.wenhao.jpa.specification.EqualSpecification;
import com.github.wenhao.jpa.specification.GeSpecification;
import com.github.wenhao.jpa.specification.GtSpecification;
import com.github.wenhao.jpa.specification.InSpecification;
import com.github.wenhao.jpa.specification.LeSpecification;
import com.github.wenhao.jpa.specification.LikeSpecification;
import com.github.wenhao.jpa.specification.LtSpecification;
import com.github.wenhao.jpa.specification.NotEqualSpecification;
import com.github.wenhao.jpa.specification.NotInSpecification;
import com.github.wenhao.jpa.specification.NotLikeSpecification;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public class Builder<T> {

    private final Predicate.BooleanOperator operator;

    private List<Specification<T>> specifications;

    public Builder(Predicate.BooleanOperator operator) {
        this.operator = operator;
        this.specifications = new ArrayList<>();
    }

    public Builder<T> eq(String property, Object... values) {
        return eq(true, property, values);
    }

    public Builder<T> eq(boolean condition, String property, Object... values) {
        return this.predicate(condition, new EqualSpecification<T>(property, values));
    }

    public Builder<T> ne(String property, Object... values) {
        return ne(true, property, values);
    }

    public Builder<T> ne(boolean condition, String property, Object... values) {
        return this.predicate(condition, new NotEqualSpecification<T>(property, values));
    }

    public Builder<T> gt(String property, Number number) {
        return gt(true, property, number);
    }

    public Builder<T> gt(boolean condition, String property, Number number) {
        return this.predicate(condition, new GtSpecification<T>(property, number));
    }

    public Builder<T> ge(String property, Number number) {
        return ge(true, property, number);
    }

    public Builder<T> ge(boolean condition, String property, Number number) {
        return this.predicate(condition, new GeSpecification<T>(property, number));
    }

    public Builder<T> lt(String property, Number number) {
        return lt(true, property, number);
    }

    public Builder<T> lt(boolean condition, String property, Number number) {
        return this.predicate(condition, new LtSpecification<T>(property, number));
    }

    public Builder<T> le(String property, Number number) {
        return le(true, property, number);
    }

    public Builder<T> le(boolean condition, String property, Number number) {
        return this.predicate(condition, new LeSpecification<T>(property, number));
    }

    public Builder<T> between(String property, Range<? extends Comparable<?>> range) {
        return between(true, property, range);
    }

    public Builder<T> between(boolean condition, String property, Range<? extends Comparable<?>> range) {
        return this.predicate(condition, new BetweenSpecification<T>(property, range));
    }

    public Builder<T> like(String property, String... patterns) {
        return like(true, property, patterns);
    }

    public Builder<T> like(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new LikeSpecification<T>(property, patterns));
    }

    public Builder<T> notLike(String property, String... patterns) {
        return notLike(true, property, patterns);
    }

    public Builder<T> notLike(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new NotLikeSpecification<T>(property, patterns));
    }

    public Builder<T> in(String property, Object... values) {
        return this.in(true, property, values);
    }

    public Builder<T> in(boolean condition, String property, Object... values) {
        return this.predicate(condition, new InSpecification<T>(property, values));
    }

    public Builder<T> notIn(String property, Object... values) {
        return this.notIn(true, property, values);
    }

    public Builder<T> notIn(boolean condition, String property, Object... values) {
        return this.predicate(condition, new NotInSpecification<T>(property, values));
    }

    public Builder<T> predicate(Specification specification) {
        return predicate(true, specification);
    }

    public Builder<T> predicate(boolean condition, Specification specification) {
        if (condition) {
            this.specifications.add(specification);
        }
        return this;
    }

    public Specification<T> build() {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate[] predicates = new Predicate[specifications.size()];
                for (int i = 0; i < specifications.size(); i++) {
                    predicates[i] = specifications.get(i).toPredicate(root, query, cb);
                }
                if(operator == OR) {
                    return cb.or(predicates);
                } else {
                    return cb.and(predicates);
                }
            }
        };
    }
}
