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
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@DataJpaTest
public class BetweenTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void should_be_able_to_find_by_using_between() throws ParseException {
        // given
        Person jack = new PersonBuilder()
                .name("Jack")
                .birthday(getDate("1987-11-14"))
                .build();
        Person eric = new PersonBuilder()
                .name("Eric")
                .birthday(getDate("1990-10-12"))
                .build();
        personRepository.save(jack);
        personRepository.save(eric);

        // when
        Specification<Person> specification = Specifications.<Person>and()
                .between(nonNull(jack.getBirthday()), Person::getBirthday, getDate("1980-01-01"), getDate("1989-12-31"))
                .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(1);
    }



    private Date getDate(String source) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(source);
    }
}
