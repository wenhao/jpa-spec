[![Build Status](https://travis-ci.org/wenhao/jpa-spec.svg?branch=master)](https://travis-ci.org/wenhao/jpa-spec)

# jpa-spec

Inspired by [Legacy Hibernate Criteria Queries], while this should be considered deprecated vs JPA APIs,

but it still productive and easily understandable. Build on Spring Data JPA and simplify the dynamic query process.

### Features

* Compatible with Spring Data JPA and JPA 2 interface.
* Equal/NotEqual/Like/NotLike/In support multiple values, Equal/NotEqual support **Null** value.
* Builder style specification creator.
* Support pagination and sort builder.

### Gradle

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.wenhao:jpa-spec:2.0.9'
}
```

### Maven

add repository [http://jcenter.bintray.com](http://jcenter.bintray.com) to maven settings.xml file.

```xml
<dependency>
    <groupId>com.github.wenhao</groupId>
    <artifactId>jpa-spec</artifactId>
    <version>2.0.9</version>
</dependency>
```

### Specification By Examples:

####Each specification support three parameters:

3. condition: if true(default), apply this specification.
1. property: field name.
2. values: compare value with model, eq/ne/like support multiple values.

####General Example

every Repository class should extends from two super class **JpaRepository** and **JpaSpecificationExecutor**.

```java
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
}    
```

```java
public Page<Person> findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .eq(StringUtils.isNotBlank(request.getName()), "name", request.getName())
            .gt(Objects.nonNull(request.getAge()), "age", 18)
            .between("birthday", new Range<>(new Date(), new Date()))
            .like("nickName", "%og%", "%me")
            .build();
            
    return personRepository.findAll(specification, new PageRequest(0, 15)); 
}
```

####Equal/NotEqual Example

find any person nickName equals to "dog" and name equals to "Jack"/"Eric" or null value, and company is null.

**Test:** [EqualTest.java] and [NotEqualTest.java]

```java
public Persons findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .eq("nickName", "dog")
            .eq(StringUtils.isNotBlank(request.getName()), "name", "Jack", "Eric", null)
            .eq("company", null) //or eq("company", (Object) null)
            .build();
            
    return personRepository.findAll(specification); 
}
```

####In/NotIn Example

find any person name in "Jack" or "Eric" and company not in "ThoughtWorks" or "IBM".

**Test:** [InTest.java]

```java
public Persons findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .in("name", request.getNames().toArray()) //or in("name", "Jack", "Eric")
            .notIn("company", "ThoughtWorks", "IBM")
            .build();
            
    return personRepository.findAll(specification); 
}
```

####Numerical Example

find any people age bigger than 18. 

**Test:** [GtTest.java]

```java
public Persons findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .gt(Objects.nonNull(request.getAge()), "age", 18)
            .build();
            
    return personRepository.findAll(specification); 
}
```

####Between Example

find any person age between 18 and 25, birthday between someday and someday.

**Test:** [BetweenTest.java]

```java
public Persons findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .between(Objects.nonNull(request.getAge(), "age", new Range<>(18, 25))
            .between("birthday", new Range<>(new Date(), new Date()))
            .build();
            
    return personRepository.findAll(specification); 
}  
```

####Like/NotLike Example

find any person name like %ac% or %og%, company not like %ec%.

**Test:** [LikeTest.java] and [NotLikeTest.java]

```java
public Page<Person> findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .like("name", "ac", "%og%")
            .notLike("company", "ec")
            .build();
            
    return personRepository.findAll(specification); 
}
```

####Pagination and Sort

find person by pagination and sort by name desc and birthday asc.

```java
public Page<Person> findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
            .eq(StringUtils.isNotBlank(request.getName()), "name", request.getName())
            .gt("age", 18)
            .between("birthday", new Range<>(new Date(), new Date()))
            .like("nickName", "%og%")
            .build();
            
    Sort sort = new Sorts()
        .desc(StringUtils.isNotBlank(request.getName()), "name")
        .asc("birthday")
        .build();
            
    return personRepository.findAll(specification, new PageRequest(0, 15, sort));
}
```

####Custom Specification

@ManyToOne association query, find perosn name equals to "Jack" and phone brand equals to "HuaWei".

**Test:** [AndTest.java]

```java
public List<Phone> findAll(SearchRequest request) {
    Specification<Phone> specification = new Specifications<Phone>()
        .eq(StringUtils.isNotBlank(request.getBrand()), "brand", "HuaWei")
        .and(StringUtils.isNotBlank(request.getPersonName()), (root, query, cb) -> {
            Path<Person> person = root.get("person");
            return cb.equal(person.get("name"), "Jack");
        })
        .build();

    return phoneRepository.findAll(specification);
}
```

####Virtual View

Using **@org.hibernate.annotations.Subselect** to define a virtual view if you don't want a database table view.

There is no difference between a view and a base table for a Hibernate mapping.

### Copyright and license

Copyright 2016 Wen Hao

Licensed under [Apache License]

[Legacy Hibernate Criteria Queries]: https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#appendix-legacy-criteria
[EqualTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/EqualTest.java 
[NotEqualTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/NotEqualTest.java 
[InTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/InTest.java
[GtTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/GtTest.java
[BetweenTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/BetweenTest.java
[LikeTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/LikeTest.java
[NotLikeTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/NotLikeTest.java 
[AndTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/AndTest.java
[Apache License]: ./LICENSE