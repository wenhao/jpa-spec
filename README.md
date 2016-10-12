[![Build Status](https://travis-ci.org/wenhao/jpa-spec.svg?branch=master)](https://travis-ci.org/wenhao/jpa-spec)

# jpa-spec

### Features

* Compatible with JPA 2 interface.
* Equal/NotEqual/Like support multiple values, Equal/NotEqual support **Null** value.
* Builder style specification creator.


### JPA 2 criteria API introduction

JPA 2 introduces a criteria API that can be used to build queries programmatically. 

Writing a criteria you actually define the where-clause of a query for a domain class.
 
Taking another step back these criteria can be regarded as predicate over the entity that is described by the JPA criteria API constraints.

```java
public interface JpaSpecificationExecutor<T> {

	T findOne(Specification<T> spec);

	List<T> findAll(Specification<T> spec);

	Page<T> findAll(Specification<T> spec, Pageable pageable);

	List<T> findAll(Specification<T> spec, Sort sort);

	long count(Specification<T> spec);
}

```

### Gradle

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.wenhao:jpa-spec:2.0.7'
}
```

### Maven

add repository [http://jcenter.bintray.com](http://jcenter.bintray.com) to maven settings.xml file.

```xml
<dependency>
    <groupId>com.github.wenhao</groupId>
    <artifactId>jpa-spec</artifactId>
    <version>2.0.7</version>
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

Person person = new Person();
person.setName("Jack");
person.setNickName("dog");
person.setAge(20);
person.setBirthday(new Date())
person.setCompany(null);

Specification<Person> specification = new Specifications<Person>()
        .eq(StringUtils.isNotBlank(person.getName()), "name", person.getName())
        .gt(Objects.nonNull(person.getAge()), "age", 18)
        .between("birthday", new Range<>(new Date(), new Date()))
        .like("nickName", "%og%", "%me")
        .build();
        
Page<Person> persons = personRepository.findAll(specification, new PageRequest(0, 15));           
```

####Equal/NotEqual Example

find any person nickName equals to "dog" and name equals to "Jack"/"Eric" or null value.

```java
Person person = new Person();
person.setNickName("dog");

Specification<Person> specification = new Specifications<Person>()
        .eq("nickName", "dog")
        .eq(StringUtils.isNotBlank(person.getName()), "name", "Jack", "Eric", null)
        .build();
        
personRepository.findAll(specification)); 
```

####Numerical Example

find any people age bigger than 18. 

```java
Person person = new Person();
person.setAge(20);

Specification<Person> specification = new Specifications<Person>()
        .gt(Objects.nonNull(person.getAge()), "age", 18)
        .build();
        
Page<Person> persons = personRepository.findAll(specification, new PageRequest(0, 15));    
```

####Between Example

find any person age between 18 and 25, birthday between someday and someday.

```java
Person person = new Person();
person.setAge(20);
person.setBirthday(new Date())

Specification<Person> specification = new Specifications<Person>()
        .between(Objects.nonNull(person.getAge(), "age", new Range<>(18, 25))
        .between("birthday", new Range<>(new Date(), new Date()))
        .build();
        
personRepository.findAll(specification);      
```

####Pagination and Sort

```java
Specification<Person> specification = new Specifications<Person>()
        .eq(StringUtils.isNotBlank(person.getName()), "name", person.getName())
        .gt("age", 18)
        .between("birthday", new Range<>(new Date(), new Date()))
        .like("nickName", "%og%")
        .build();
        
Sort sort = new Sort(new Order(DESC, "name"), new Order(ASC, "birthday"));
        
Page<Person> persons = personRepository.findAll(specification, new PageRequest(0, 15, sort));
long totalCount = personRepository.count(specification);        
```
