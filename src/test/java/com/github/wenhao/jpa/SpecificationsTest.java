package com.github.wenhao.jpa;

import static java.util.Objects.nonNull;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.junit.Test;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;

import com.github.wenhao.jpa.model.Person;

public class SpecificationsTest {
    @Test
    public void should_be_able_to_get_specification() {
        // given

        // when
        Specification<Person> specification = new Specifications<Person>()
                .eq("name", "Jack")
                .ne("company", "ThoughtWorks", isNotBlank("ThoughtWorks"))
                .gt("age", 1)
                .ge("age", 2)
                .lt("age", 3)
                .le("age", 4)
                .between("age", new Range<>(1, 20))
                .between("birthday", new Range<>(new Date(), new Date()), nonNull(new Range<>(1, 2)))
                .like("nickName", "*Jack")
                .build();
        // then
    }
}
