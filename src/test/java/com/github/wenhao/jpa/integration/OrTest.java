package com.github.wenhao.jpa.integration;

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

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
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
            .like("name", "%ac%")
            .gt("age", 19)
            .eq(jack.getCompany() != null, null)
            .ne(jack.getNickName() != null, null)
            .between(jack.getBirthday() != null, "birthday", new Date(), new Date())
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
                .eq(isNotBlank(EMPTY), "name", jack.getName())
                .like(isNotBlank(EMPTY), "name", "%" + jack.getName() + "%")
                .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(2);
    }
}
