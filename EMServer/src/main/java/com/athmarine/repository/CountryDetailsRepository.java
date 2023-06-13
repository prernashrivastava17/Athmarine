package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.CountryDetails;

@Repository
public interface CountryDetailsRepository extends JpaRepository<CountryDetails, Integer> {
	
	List<CountryDetails> findAllByOrderByCallingCodeAsc();

}
