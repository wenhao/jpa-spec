[![Build Status](https://travis-ci.org/wenhao/jpa-spec.svg?branch=master)](https://travis-ci.org/wenhao/jpa-spec)

# jpa-spec

Inspired by [Legacy Hibernate Criteria Queries], while this should be considered deprecated vs JPA APIs,

but it still productive and easily understandable. Build on Spring Data JPA and simplify the dynamic query process.

### Features

* Compatible with Spring Data JPA and JPA 2 interface.
* Equal/NotEqual/Like/NotLike/In/NotIn support multiple values, Equal/NotEqual support **Null** value.
* Each specification support join query(inner joiner). 
* Support custom specification.
* Builder style specification creator.
* Support pagination and sort builder.

### Gradle

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.wenhao:jpa-spec:2.2.1'
}
```

### Maven

```xml
<dependency>
    <groupId>com.github.wenhao</groupId>
    <artifactId>jpa-spec</artifactId>
    <version>2.2.1</version>
</dependency>
```

### Specification By Examples:

####Each specification support three parameters:

3. condition: if true(default), apply this specification.
1. property: field name.
2. values: compare value with model, eq/ne/like support multiple values.

####General Example

each Repository class should extends from two super class **JpaRepository** and **JpaSpecificationExecutor**.

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

####Join

each specification support association query as inner join. 

**Test:** [JoinTest.java]

@ManyToOne association query, find person name equals to "Jack" and phone brand equals to "HuaWei".
    
```java
public List<Phone> findAll(SearchRequest request) {
    Specification<Phone> specification = new Specifications<Phone>()
        .eq(StringUtils.isNotBlank(request.getBrand()), "brand", "HuaWei")
        .eq(StringUtils.isNotBlank(request.getPersonName()), "person.name", "Jack")
        .build();

    return phoneRepository.findAll(specification);
}
```

@ManyToMany association query, find person age between 10 and 35, live in "Chengdu" street.

```java
public List<Phone> findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
        .between("age", new Range<>(10, 35))
        .eq(StringUtils.isNotBlank(jack.getName()), "addresses.street", "Chengdu")
        .build();

    return phoneRepository.findAll(specification);
}
```

####Custom Specification

You can custom specification to do the @ManyToOne and @ManyToMany as well.

@ManyToOne association query, find person name equals to "Jack" and phone brand equals to "HuaWei".

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

@ManyToMany association query, find person age between 10 and 35, live in "Chengdu" street.

**Test:** [AndTest.java]

```java
public List<Phone> findAll(SearchRequest request) {
    Specification<Person> specification = new Specifications<Person>()
        .between("age", new Range<>(10, 35))
        .and(StringUtils.isNotBlank(jack.getName()), ((root, query, cb) -> {
            Join address = root.join("addresses", JoinType.LEFT);
            return cb.equal(address.get("street"), "Chengdu");
        }))
        .build();

    return phoneRepository.findAll(specification);
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

####Virtual View

Using **@org.hibernate.annotations.Subselect** to define a virtual view if you don't want a database table view.

There is no difference between a view and a base table for a Hibernate mapping.

**Test:** [VirtualViewTest.java]

```java
@Entity
@Immutable
@Subselect("SELECT p.id id, p.name name, p.age age, ic.number number " +
           "FROM person p " +
           "LEFT JOIN id_card ic " +
           "ON p.id_card_id=ic.id")
public class PersonIdCard {
    @Id
    private Long id;
    private String name;
    private Integer age;
    private String number;
    
    // getter and setter
```

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
[JoinTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/JoinTest.java
[VirtualViewTest.java]: ./src/test/java/com/github/wenhao/jpa/integration/VirtualViewTest.java
[Apache License]: ./LICENSE