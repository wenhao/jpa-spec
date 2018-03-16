package com.github.wenhao.jpa.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.wenhao.jpa.Specifications;
import com.github.wenhao.jpa.builder.PersonBuilder;
import com.github.wenhao.jpa.model.Person;
import com.github.wenhao.jpa.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NotEqualTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void should_be_able_to_find_by_using_not_equal() {
        // given
        Person jack = new PersonBuilder()
            .name("Jack")
            .nickName("Dog")
            .company("company-name")
            .build();
        Person eric = new PersonBuilder()
            .name("Eric")
            .nickName("Cat")
            .company("company-name")
            .build();
        personRepository.save(jack);
        personRepository.save(eric);

        // when
        Specification<Person> specification = Specifications.<Person>and()
            .eq("company", "company-name")
            .ne(StringUtils.isNotBlank(jack.getName()), "name", jack.getName())
            .ne("nickName", jack.getNickName(), "Aaron")
            .build();

        Optional<Person> person = personRepository.findOne(specification);

        // then
        assertThat(person.get().getName()).isEqualTo(eric.getName());
    }

    @Test
    public void should_be_able_to_find_by_using_not_equal_for_single_null_value() {
        // given
        Person jack = new PersonBuilder()
            .name("Jack")
            .age(18)
            .company("Abc")
            .build();
        Person eric = new PersonBuilder()
            .name("Eric")
            .age(20)
            .build();

        personRepository.save(jack);
        personRepository.save(eric);

        // when
        Specification<Person> specification = Specifications.<Person>and()
            .ne("name", null)
            .ne("company", (Object) null)
            .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(1);
    }
}
