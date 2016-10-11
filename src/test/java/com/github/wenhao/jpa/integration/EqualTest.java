package com.github.wenhao.jpa.integration;

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
public class EqualTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void should_be_able_to_find_by_using_equal() {
        // given
        Person person = new PersonBuilder()
            .name("Jack")
            .age(18)
            .build();
        personRepository.save(person);

        // when
        Specification<Person> specification = new Specifications<Person>()
            .eq("name", person.getName())
            .build();

        Person result = personRepository.findOne(specification);

        // then
        assertThat(result.getName()).isEqualTo(person.getName());
    }

    @Test
    public void should_be_able_to_find_by_using_equal_with_multiple_values() {
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
        Specification<Person> specification = new Specifications<Person>()
            .eq("name", jack.getName(), eric.getName(), null)
            .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(3);
    }
}
