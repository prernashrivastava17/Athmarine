package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.SparesCommission;

@Repository
public interface SparesCommissionRepository extends JpaRepository<SparesCommission, Integer> {

}
