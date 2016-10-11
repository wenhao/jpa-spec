package com.github.wenhao.jpa.builder;

import com.github.wenhao.jpa.model.Person;

import java.util.Date;

public class PersonBuilder {
    private Person person;

    public PersonBuilder() {
        this.person = new Person();
    }

    public PersonBuilder name(String name) {
        this.person.setName(name);
        return this;
    }

    public PersonBuilder age(Integer age) {
        this.person.setAge(age);
        return this;
    }

    public PersonBuilder nickName(String nickName) {
        this.person.setNickName(nickName);
        return this;
    }

    public PersonBuilder company(String company) {
        this.person.setCompany(company);
        return this;
    }

    public PersonBuilder birthday(Date birthday) {
        this.person.setBirthday(birthday);
        return this;
    }

    public Person build() {
        return this.person;
    }
}
