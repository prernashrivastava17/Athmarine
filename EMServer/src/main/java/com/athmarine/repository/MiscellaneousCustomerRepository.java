package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MiscellaneousCustomer;

@Repository
public interface MiscellaneousCustomerRepository extends JpaRepository<MiscellaneousCustomer, Integer> {

}
