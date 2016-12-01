package com.github.wenhao.jpa;

import com.github.wenhao.jpa.specification.AndSpecification;
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

public class OrSpecifications<T> {
    private List<Specification<T>> specifications;

    public OrSpecifications() {
        this.specifications = new ArrayList<Specification<T>>();
    }

    public OrSpecifications<T> eq(String property, Object... values) {
        return eq(true, property, values);
    }

    public OrSpecifications<T> eq(boolean condition, String property, Object... values) {
        if (condition) {
            this.specifications.add(new EqualSpecification<T>(property, values));
        }
        return this;
    }

    public OrSpecifications<T> ne(String property, Object... values) {
        return ne(true, property, values);
    }

    public OrSpecifications<T> ne(boolean condition, String property, Object... values) {
        if (condition) {
            this.specifications.add(new NotEqualSpecification<T>(property, values));
        }
        return this;
    }

    public OrSpecifications<T> gt(String property, Number number) {
        return gt(true, property, number);
    }

    public OrSpecifications<T> gt(boolean condition, String property, Number number) {
        if (condition) {
            this.specifications.add(new GtSpecification<T>(property, number));
        }
        return this;
    }

    public OrSpecifications<T> ge(String property, Number number) {
        return ge(true, property, number);
    }

    public OrSpecifications<T> ge(boolean condition, String property, Number number) {
        if (condition) {
            this.specifications.add(new GeSpecification<T>(property, number));
        }
        return this;
    }

    public OrSpecifications<T> lt(String property, Number number) {
        return lt(true, property, number);
    }

    public OrSpecifications<T> lt(boolean condition, String property, Number number) {
        if (condition) {
            this.specifications.add(new LtSpecification<T>(property, number));
        }
        return this;
    }

    public OrSpecifications<T> le(String property, Number number) {
        return le(true, property, number);
    }

    public OrSpecifications<T> le(boolean condition, String property, Number number) {
        if (condition) {
            this.specifications.add(new LeSpecification<T>(property, number));
        }
        return this;
    }

    public OrSpecifications<T> between(String property, Range<? extends Comparable<?>> range) {
        return between(true, property, range);
    }

    public OrSpecifications<T> between(boolean condition, String property, Range<? extends Comparable<?>> range) {
        if (condition) {
            this.specifications.add(new BetweenSpecification<T>(property, range));
        }
        return this;
    }

    public OrSpecifications<T> like(String property, String... patterns) {
        return like(true, property, patterns);
    }

    public OrSpecifications<T> like(boolean condition, String property, String... patterns) {
        if (condition) {
            this.specifications.add(new LikeSpecification<T>(property, patterns));
        }
        return this;
    }

    public OrSpecifications<T> notLike(String property, String... patterns) {
        return notLike(true, property, patterns);
    }

    public OrSpecifications<T> notLike(boolean condition, String property, String... patterns) {
        if (condition) {
            this.specifications.add(new NotLikeSpecification<T>(property, patterns));
        }
        return this;
    }

    public OrSpecifications<T> in(String property, Object... values) {
        return this.in(true, property, values);
    }

    public OrSpecifications<T> in(boolean condition, String property, Object... values) {
        if (condition) {
            this.specifications.add(new InSpecification<T>(property, values));
        }
        return this;
    }

    public OrSpecifications<T> notIn(String property, Object... values) {
        return this.notIn(true, property, values);
    }

    public OrSpecifications<T> notIn(boolean condition, String property, Object... values) {
        if (condition) {
            this.specifications.add(new NotInSpecification<T>(property, values));
        }
        return this;
    }

    public OrSpecifications<T> and(Specification specification) {
        return and(true, specification);
    }

    public OrSpecifications<T> and(boolean condition, Specification specification) {
        if (condition) {
            this.specifications.add(new AndSpecification<T>(specification));
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
                return cb.or(predicates);
            }
        };
    }
}
