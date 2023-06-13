package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.AvailableOn;

@Repository
public interface AvailableOnRepository extends JpaRepository<AvailableOn, Integer> {

	Boolean existsByAppName(String appName);
	
}
