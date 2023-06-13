package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.WorkingCommission;

@Repository
public interface WorkingCommissionRepository extends JpaRepository<WorkingCommission, Integer> {

}
