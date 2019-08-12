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

package com.github.wenhao.jpa.integration;

import com.github.wenhao.jpa.Specifications;
import com.github.wenhao.jpa.builder.PersonBuilder;
import com.github.wenhao.jpa.model.Person;
import com.github.wenhao.jpa.repository.PersonRepository;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class OrTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void should_be_able_to_find_by_using_or_with_multiple_values() {
        // given
        Person jack = new PersonBuilder()
                .name("Jack")
                .age(18)
                .build();
        Person eric = new PersonBuilder()
                .name("Eric")
                .age(20)
                .build();
        Person jackson = new PersonBuilder()
                .age(30)
                .nickName("Jackson")
                .build();
        personRepository.save(jack);
        personRepository.save(eric);
        personRepository.save(jackson);

        // when
        Specification<Person> specification = Specifications.<Person>or()
                .like(Person::getName, "%ac%")
                .gt(Person::getAge, 19)
                .eq(jack.getCompany() != null, null)
                .ne(jack.getNickName() != null, null)
                .between(jack.getBirthday() != null, Person::getBirthday, new Date(), new Date())
                .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(3);
    }

    @Test
    public void should_be_able_to_find_all_if_all_predicate_are_null() {
        // given
        Person jack = new PersonBuilder()
                .name("Jack")
                .age(18)
                .build();
        Person eric = new PersonBuilder()
                .name("Eric")
                .age(20)
                .build();
        personRepository.save(jack);
        personRepository.save(eric);

        // when
        Specification<Person> specification = Specifications.<Person>or()
                .eq(isNotBlank(EMPTY), Person::getName, jack.getName())
                .like(isNotBlank(EMPTY), Person::getName, "%" + jack.getName() + "%")
                .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(2);
    }
}
