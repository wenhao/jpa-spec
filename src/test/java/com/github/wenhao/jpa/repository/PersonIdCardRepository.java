package com.github.wenhao.jpa.repository;

import com.github.wenhao.jpa.model.PersonIdCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonIdCardRepository extends JpaRepository<PersonIdCard, Long>, JpaSpecificationExecutor<PersonIdCard> {
}
