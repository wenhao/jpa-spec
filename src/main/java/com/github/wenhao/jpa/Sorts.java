package com.github.wenhao.jpa;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

public class Sorts {

    private List<Order> orders;

    public Sorts() {
        this.orders = new ArrayList<Order>();
    }

    public Sorts asc(String property) {
        return asc(true, property);
    }

    public Sorts desc(String property) {
        return desc(true, property);
    }

    public Sorts asc(boolean condition, String property) {
        if (condition) {
            orders.add(new Order(ASC, property));
        }
        return this;
    }

    public Sorts desc(boolean condition, String property) {
        if (condition) {
            orders.add(new Order(DESC, property));
        }
        return this;
    }

    public Sort build() {
        return new Sort(orders);
    }
}
