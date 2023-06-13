package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.SparesCustomer;

@Repository
public interface SparesCustomerRepository extends JpaRepository<SparesCustomer, Integer>{

}