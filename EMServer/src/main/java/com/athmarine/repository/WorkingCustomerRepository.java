package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.WorkingCustomer;

@Repository
public interface WorkingCustomerRepository extends JpaRepository<WorkingCustomer, Integer>{

}
