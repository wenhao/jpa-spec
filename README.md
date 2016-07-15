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

Generate Specification easy way:

```java
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
        
personRepository.findAll(specification)        
```