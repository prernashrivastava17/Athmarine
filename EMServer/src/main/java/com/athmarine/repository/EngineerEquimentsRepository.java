package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.EngineerEquiments;
import com.athmarine.entity.User;

@Repository
public interface EngineerEquimentsRepository extends JpaRepository<EngineerEquiments, Integer> {

	public EngineerEquiments getEngineerEquimentsByEngineerId(User id);
	
	public EngineerEquiments findByEngineerId(User engineerId);
}
