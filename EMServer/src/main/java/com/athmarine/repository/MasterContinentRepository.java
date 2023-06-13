package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTContinent;

@Repository
public interface MasterContinentRepository extends JpaRepository<MSTContinent, Integer> {

}
