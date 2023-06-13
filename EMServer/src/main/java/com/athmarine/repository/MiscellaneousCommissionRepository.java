package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MiscellaneousCommission;

@Repository
public interface MiscellaneousCommissionRepository extends JpaRepository<MiscellaneousCommission, Integer> {

}
