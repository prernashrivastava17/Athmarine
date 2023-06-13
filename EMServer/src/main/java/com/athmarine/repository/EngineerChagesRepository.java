package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.EngineerCharges;

@Repository
public interface EngineerChagesRepository extends JpaRepository<EngineerCharges, Integer> {

}
