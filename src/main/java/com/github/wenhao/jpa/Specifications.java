package com.github.wenhao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;

import com.github.wenhao.jpa.specification.BetweenSpecification;
import com.github.wenhao.jpa.specification.EqualSpecification;
import com.github.wenhao.jpa.specification.GeSpecification;
import com.github.wenhao.jpa.specification.GtSpification;
import com.github.wenhao.jpa.specification.LeSpecification;
import com.github.wenhao.jpa.specification.LikeSpecification;
import com.github.wenhao.jpa.specification.LtSpecification;
import com.github.wenhao.jpa.specification.NotEqualSpecification;

public class Specifications<T> {
    private List<Specification<T>> specifications = new ArrayList<>();

    public Specifications<T> eq(String property, Object object) {
        return eq(property, object, true);
    }

    public Specifications<T> eq(String property, Object object, boolean condition) {
        if (condition) {
            this.specifications.add(new EqualSpecification<T>(property, object));
        }
        return this;
    }

    public Specifications<T> ne(String property, Object object) {
        return ne(property, object, true);
    }


    public Specifications<T> ne(String property, Object object, boolean condition) {
        if (condition) {
            this.specifications.add(new NotEqualSpecification<T>(property, object));
        }
        return this;
    }

    public Specifications<T> gt(String property, Number number) {
        return gt(property, number, true);
    }

    public Specifications<T> gt(String property, Number number, boolean condition) {
        if (condition) {
            this.specifications.add(new GtSpification<T>(property, number));
        }
        return this;
    }

    public Specifications<T> ge(String property, Number number) {
        return ge(property, number, true);
    }

    public Specifications<T> ge(String property, Number number, boolean condition) {
        if (condition) {
            this.specifications.add(new GeSpecification<T>(property, number));
        }
        return this;
    }

    public Specifications<T> lt(String property, Number number) {
        return lt(property, number, true);
    }

    public Specifications<T> lt(String property, Number number, boolean condition) {
        if (condition) {
            this.specifications.add(new LtSpecification<T>(property, number));
        }
        return this;
    }

    public Specifications<T> le(String property, Number number) {
        return le(property, number, true);
    }

    public Specifications<T> le(String property, Number number, boolean condition) {
        if (condition) {
            this.specifications.add(new LeSpecification<T>(property, number));
        }
        return this;
    }

    public Specifications<T> between(String property, Range<? extends Comparable<?>> range) {
        return between(property, range, true);
    }

    public Specifications<T> between(String property, Range<? extends Comparable<?>> range, boolean condition) {
        if (condition) {
            this.specifications.add(new BetweenSpecification<T>(property, range));
        }
        return this;
    }

    public Specifications<T> like(String property, String pattern) {
        return like(property, pattern, true);
    }

    public Specifications<T> like(String property, String pattern, boolean condition) {
        if (condition) {
            this.specifications.add(new LikeSpecification<T>(property, pattern));
        }
        return this;
    }

    public Specification<T> build() {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = this.specifications.stream()
                    .map(spec -> spec.toPredicate(root, query, criteriaBuilder))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.and(predicates);
        };
    }

}
