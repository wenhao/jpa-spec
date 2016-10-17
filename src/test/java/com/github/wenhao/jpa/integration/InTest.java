package com.github.wenhao.jpa.integration;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.wenhao.jpa.Specifications;
import com.github.wenhao.jpa.builder.PersonBuilder;
import com.github.wenhao.jpa.model.Person;
import com.github.wenhao.jpa.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void should_be_able_to_find_by_using_in() {
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
        Specification<Person> specification = new Specifications<Person>()
            .in(isNotBlank(jack.getName()), "name", "Jack", "Eric")
            .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(2);
    }
}
