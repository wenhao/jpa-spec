package com.github.wenhao.jpa.integration;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
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
public class NotLikeTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void should_be_able_to_find_by_using_not_like() {
        // given
        Person jack = new PersonBuilder()
            .name("Jack")
            .age(18)
            .nickName("Dog")
            .build();
        Person eric = new PersonBuilder()
            .name("Eric")
            .nickName("Cat")
            .age(20)
            .build();
        personRepository.save(jack);
        personRepository.save(eric);

        // when
        Specification<Person> specification = Specifications.<Person>builder()
            .notLike(isNotBlank(jack.getName()), "name", "%ac%")
            .notLike(isNotBlank(jack.getNickName()), "name", "%og%", "%ri%")
            .build();

        List<Person> persons = personRepository.findAll(specification);

        // then
        assertThat(persons.size()).isEqualTo(1);
    }

}
