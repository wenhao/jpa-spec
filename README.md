[![Build Status](https://travis-ci.org/wenhao/jpa-spec.svg?branch=master)](https://travis-ci.org/wenhao/jpa-spec)

# jpa-spec

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

### Specification By Examples:

####Each specification support three parameters:

1. property: field name.
2. value: compare value with model.
3. condition: if true(default), apply this specification.

```java
//query by person model
Person person = new Person();
person.setName("Jack");
person.setNickName("dog);
person.setAge(20);
person.setBirthday(new Date())
person.setCompany(null);

Specification<Person> specification = new Specifications<Person>()
        .eq("name", person.getName(), StringUtils.isNotBlank(person.getName()))
        .gt("age", 18, Objects.nonNull(person.getAge()))
        .between("birthday", new Range<>(new Date(), new Date()))
        .like("nickName", "*og")
        .build();
        
personRepository.findAll(specification, new PageRequest(0, 15));           
```

####Sort by fields:

```java
Specification<Person> specification = new Specifications<Person>()
        .eq("name", person.getName(), StringUtils.isNotBlank(person.getName()))
        .gt("age", 18)
        .between("birthday", new Range<>(new Date(), new Date()))
        .like("nickName", "*og")
        .build();
        
Sort sort = new Sort("name", DESC);        
        
personRepository.findAll(specification, new PageRequest(0, 15, sort));        
```