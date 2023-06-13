package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.CustomerCompany;

@Repository
public interface CustomerCompanyRepository extends JpaRepository<CustomerCompany, Integer> {

	public Boolean existsById(int id);
	
	public Boolean existsByReferralCode(String referralCode);
	
	public CustomerCompany findFirstByOrderByIdDesc();
	
}
