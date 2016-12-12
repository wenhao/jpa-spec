package com.github.wenhao.jpa.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.wenhao.jpa.Specifications;
import com.github.wenhao.jpa.builder.PersonBuilder;
import com.github.wenhao.jpa.model.Person;
import com.github.wenhao.jpa.model.PersonIdCard;
import com.github.wenhao.jpa.repository.PersonIdCardRepository;
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
public class VirtualViewTest {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonIdCardRepository personIdCardRepository;

    @Test
    public void should_be_able_to_query_from_virtual_view() {
        // given
        Person jack = new PersonBuilder()
            .name("Jack")
            .age(18)
            .idCard("100000000000000000")
            .build();
        Person eric = new PersonBuilder()
            .name("Eric")
            .age(20)
            .idCard("200000000000000000")
            .build();
        Person jackson = new PersonBuilder()
            .age(30)
            .nickName("Jackson")
            .idCard("300000000000000000")
            .build();
        personRepository.save(jack);
        personRepository.save(eric);
        personRepository.save(jackson);

        // when
        Specification<PersonIdCard> specification = Specifications.<PersonIdCard>builder()
            .gt("age", 18)
            .build();
        List<PersonIdCard> personIdCards = personIdCardRepository.findAll(specification);

        // then
        assertThat(personIdCards.size()).isEqualTo(2);
    }
}
