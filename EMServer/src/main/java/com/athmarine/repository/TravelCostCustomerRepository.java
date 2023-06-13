package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.TravelCostCustomer;

@Repository
public interface TravelCostCustomerRepository extends JpaRepository<TravelCostCustomer, Integer>{

}