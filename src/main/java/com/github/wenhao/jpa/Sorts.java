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

import com.github.wenhao.jpa.lambda.StaticMethodReferenceColumn;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

public final class Sorts {

    private Sorts() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Order> orders;

        public Builder() {
            this.orders = new ArrayList<>();
        }

        public <T> Builder asc(StaticMethodReferenceColumn<T> staticMethodReferenceColumn) {
            return asc(true, staticMethodReferenceColumn);
        }

        public Builder asc(String property) {
            return asc(true, property);
        }

        public <T> Builder desc(StaticMethodReferenceColumn<T> staticMethodReferenceColumn) {
            return desc(true, staticMethodReferenceColumn);
        }

        public Builder desc(String property) {
            return desc(true, property);
        }

        public <T> Builder asc(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn) {
            return asc(condition, staticMethodReferenceColumn.getColumn());
        }

        public Builder asc(boolean condition, String property) {
            if (condition) {
                orders.add(new Order(ASC, property));
            }
            return this;
        }

        public <T> Builder desc(boolean condition, StaticMethodReferenceColumn<T> staticMethodReferenceColumn) {
            return desc(condition, staticMethodReferenceColumn.getColumn());
        }

        public Builder desc(boolean condition, String property) {
            if (condition) {
                orders.add(new Order(DESC, property));
            }
            return this;
        }

        public Sort build() {
            return Sort.by(orders);
        }
    }
}
