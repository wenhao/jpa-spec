/*
 * Copyright © 2019, Wen Hao <wenhao@126.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.wenhao.jpa;

import com.github.wenhao.jpa.lambda.MethodReferenceColumn;
import com.github.wenhao.jpa.lambda.StaticMethodReferenceColumn;
import com.github.wenhao.jpa.specification.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public class PredicateBuilder<T> {

    private final Predicate.BooleanOperator operator;

    private List<Specification<T>> specifications;

    public PredicateBuilder(Predicate.BooleanOperator operator) {
        this.operator = operator;
        this.specifications = new ArrayList<>();
    }


    public PredicateBuilder<T> eq(MethodReferenceColumn<Object> methodReferenceColumn) {
        return eq(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> eq(boolean condition, MethodReferenceColumn<Object> methodReferenceColumn) {
        return eq(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> eq(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Object... values) {
        return eq(true, staticMethodReferenceColumn, values);
    }

    public PredicateBuilder<T> eq(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Object... values) {
        return eq(condition, staticMethodReferenceColumn.getColumn(), values);
    }

    public PredicateBuilder<T> eq(String property, Object... values) {
        return eq(true, property, values);
    }

    public PredicateBuilder<T> eq(boolean condition, String property, Object... values) {
        return this.predicate(condition, new EqualSpecification<T>(property, values));
    }


    public PredicateBuilder<T> ne(MethodReferenceColumn<Object> methodReferenceColumn) {
        return ne(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> ne(boolean condition, MethodReferenceColumn<Object> methodReferenceColumn) {
        return ne(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> ne(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Object... values) {
        return ne(true, staticMethodReferenceColumn, values);
    }

    public PredicateBuilder<T> ne(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Object... values) {
        return ne(condition, staticMethodReferenceColumn.getColumn(), values);
    }

    public PredicateBuilder<T> ne(String property, Object... values) {
        return ne(true, property, values);
    }

    public PredicateBuilder<T> ne(boolean condition, String property, Object... values) {
        return this.predicate(condition, new NotEqualSpecification<T>(property, values));
    }

    public PredicateBuilder<T> gt(MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return gt(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> gt(boolean condition, MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return gt(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> gt(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return gt(true, staticMethodReferenceColumn, compare);
    }

    public PredicateBuilder<T> gt(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return gt(condition, staticMethodReferenceColumn.getColumn(), compare);
    }

    public PredicateBuilder<T> gt(String property, Comparable<?> compare) {
        return gt(true, property, compare);
    }

    public PredicateBuilder<T> gt(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new GtSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> ge(MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return ge(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> ge(boolean condition, MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return ge(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> ge(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return ge(true, staticMethodReferenceColumn, compare);
    }

    public PredicateBuilder<T> ge(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return ge(condition, staticMethodReferenceColumn.getColumn(), compare);
    }

    public PredicateBuilder<T> ge(String property, Comparable<?> compare) {
        return ge(true, property, compare);
    }

    public PredicateBuilder<T> ge(boolean condition, String property, Comparable<? extends Object> compare) {
        return this.predicate(condition, new GeSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> lt(MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return lt(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> lt(boolean condition, MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return lt(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> lt(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return lt(true, staticMethodReferenceColumn, compare);
    }

    public PredicateBuilder<T> lt(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return lt(condition, staticMethodReferenceColumn.getColumn(), compare);
    }

    public PredicateBuilder<T> lt(String property, Comparable<?> number) {
        return lt(true, property, number);
    }

    public PredicateBuilder<T> lt(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new LtSpecification<T>(property, compare));
    }


    public PredicateBuilder<T> le(MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return le(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> le(boolean condition, MethodReferenceColumn<Comparable<?>> methodReferenceColumn) {
        return le(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> le(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return le(true, staticMethodReferenceColumn, compare);
    }

    public PredicateBuilder<T> le(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Comparable<?> compare) {
        return le(condition, staticMethodReferenceColumn.getColumn(), compare);
    }

    public PredicateBuilder<T> le(String property, Comparable<?> compare) {
        return le(true, property, compare);
    }

    public PredicateBuilder<T> le(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new LeSpecification<T>(property, compare));
    }


    public PredicateBuilder<T> between(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Object lower, Object upper) {
        return between(true, staticMethodReferenceColumn, lower, upper);
    }

    public PredicateBuilder<T> between(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Object lower, Object upper) {
        return between(condition, staticMethodReferenceColumn.getColumn(), lower, upper);
    }

    public PredicateBuilder<T> between(String property, Object lower, Object upper) {
        return between(true, property, lower, upper);
    }

    public PredicateBuilder<T> between(boolean condition, String property, Object lower, Object upper) {
        return this.predicate(condition, new BetweenSpecification<T>(property, lower, upper));
    }

    public PredicateBuilder<T> like(MethodReferenceColumn<String> methodReferenceColumn) {
        return like(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> like(boolean condition, MethodReferenceColumn<String> methodReferenceColumn) {
        return like(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> like(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, String... patterns) {
        return like(true, staticMethodReferenceColumn, patterns);
    }

    public PredicateBuilder<T> like(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, String... patterns) {
        return like(condition, staticMethodReferenceColumn.getColumn(), patterns);
    }

    public PredicateBuilder<T> like(String property, String... patterns) {
        return like(true, property, patterns);
    }

    public PredicateBuilder<T> like(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new LikeSpecification<T>(property, patterns));
    }

    public PredicateBuilder<T> notLike(MethodReferenceColumn<String> methodReferenceColumn) {
        return notLike(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> notLike(boolean condition, MethodReferenceColumn<String> methodReferenceColumn) {
        return notLike(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> notLike(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, String... patterns) {
        return notLike(true, staticMethodReferenceColumn, patterns);
    }

    public PredicateBuilder<T> notLike(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, String... patterns) {
        return notLike(condition, staticMethodReferenceColumn.getColumn(), patterns);
    }

    public PredicateBuilder<T> notLike(String property, String... patterns) {
        return notLike(true, property, patterns);
    }

    public PredicateBuilder<T> notLike(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new NotLikeSpecification<T>(property, patterns));
    }


    public PredicateBuilder<T> in(MethodReferenceColumn<Collection<?>> methodReferenceColumn) {
        return in(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> in(boolean condition, MethodReferenceColumn<Collection<?>> methodReferenceColumn) {
        return in(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> in(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Collection<?> values) {
        return in(true, staticMethodReferenceColumn, values);
    }

    public PredicateBuilder<T> in(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Collection<?> values) {
        return in(condition, staticMethodReferenceColumn.getColumn(), values);
    }

    public PredicateBuilder<T> in(String property, Collection<?> values) {
        return this.in(true, property, values);
    }

    public PredicateBuilder<T> in(boolean condition, String property, Collection<?> values) {
        return this.predicate(condition, new InSpecification<T>(property, values));
    }

    public PredicateBuilder<T> notIn(MethodReferenceColumn<Collection<?>> methodReferenceColumn) {
        return notIn(true, methodReferenceColumn);
    }

    public PredicateBuilder<T> notIn(boolean condition, MethodReferenceColumn<Collection<?>> methodReferenceColumn) {
        return notIn(condition, methodReferenceColumn.getColumn(), methodReferenceColumn.get());
    }

    public PredicateBuilder<T> notIn(StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Collection<?> values) {
        return notIn(true, staticMethodReferenceColumn, values);
    }

    public PredicateBuilder<T> notIn(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn, Collection<?> values) {
        return notIn(condition, staticMethodReferenceColumn.getColumn(), values);
    }

    public PredicateBuilder<T> notIn(String property, Collection<?> values) {
        return this.notIn(true, property, values);
    }

    public PredicateBuilder<T> notIn(boolean condition, String property, Collection<?> values) {
        return this.predicate(condition, new NotInSpecification<T>(property, values));
    }

    public PredicateBuilder<T> predicate(Specification specification) {
        return predicate(true, specification);
    }

    public PredicateBuilder<T> predicate(boolean condition, Specification specification) {
        if (condition) {
            this.specifications.add(specification);
        }
        return this;
    }

    public PredicateBuilder<T> predicate(boolean condition, Consumer<PredicateBuilder<T>> consumer) {
        if (condition) {
            consumer.accept(this);
        }
        return this;
    }

    public PredicateBuilder<T> predicate(boolean condition, Supplier<Specification> consumer) {
        if (condition) {
            this.specifications.add(consumer.get());
        }
        return this;
    }

    public Specification<T> build() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate[] predicates = new Predicate[specifications.size()];
            for (int i = 0; i < specifications.size(); i++) {
                predicates[i] = specifications.get(i).toPredicate(root, query, cb);
            }
            if (Objects.equals(predicates.length, 0)) {
                return null;
            }
            return OR.equals(operator) ? cb.or(predicates) : cb.and(predicates);
        };
    }
}
