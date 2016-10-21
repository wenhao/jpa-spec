package com.github.wenhao.jpa.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.wenhao.jpa.Specifications;
import com.github.wenhao.jpa.builder.PersonBuilder;
import com.github.wenhao.jpa.model.Person;
import com.github.wenhao.jpa.model.Phone;
import com.github.wenhao.jpa.repository.PersonRepository;
import com.github.wenhao.jpa.repository.PhoneRepository;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.Path;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AndTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PhoneRepository phoneRepository;

    @Test
    public void should_be_able_to_find_by_using_many_to_one_query() {
        // given
        Person jack = new PersonBuilder()
            .name("Jack")
            .age(18)
            .phone("iPhone", "139000000000")
            .phone("HuaWei", "13600000000")
            .phone("HuaWei", "18000000000")
            .phone("Samsung", "13600000000")
            .build();

        Set<Phone> jackPhones = jack.getPhones();
        for (Phone phone : jackPhones) {
            phone.setPerson(jack);
        }
        personRepository.save(jack);

        // when
        Specification<Phone> specification = new Specifications<Phone>()
            .eq("brand", "HuaWei")
            .and(StringUtils.isNotBlank(jack.getName()), (root, query, cb) -> {
                Path<Person> person = root.get("person");
                return cb.equal(person.get("name"), jack.getName());
            })
            .build();

        List<Phone> phones = phoneRepository.findAll(specification);

        // then
        assertThat(phones.size()).isEqualTo(2);
    }
}
