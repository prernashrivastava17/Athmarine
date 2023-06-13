package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.TravelCommission;

@Repository
public interface TravelCommissionRepository extends JpaRepository<TravelCommission, Integer> {

}
