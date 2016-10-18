package com.github.wenhao.jpa.repository;

import com.github.wenhao.jpa.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhoneRepository extends JpaRepository<Phone, Long>, JpaSpecificationExecutor<Phone> {
}
