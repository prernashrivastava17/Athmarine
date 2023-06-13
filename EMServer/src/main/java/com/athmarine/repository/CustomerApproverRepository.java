package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.CustomerApprover;

@Repository
public interface CustomerApproverRepository extends JpaRepository<CustomerApprover, Integer>{

}
