package com.athmarine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.athmarine.entity.VendorCompany;

public interface VendorCompanyRepository extends CrudRepository<VendorCompany, Integer> {

//	public Optional<VendorCompany> findById(Integer id);

	public VendorCompany findByReferralCode(String referralCode);

	public Boolean existsByReferralCode(String referralCode);

	public Boolean existsById(int id);

	@Query(value = "Select MAX(id) From vendor_company ", nativeQuery = true)
	Integer findVendorCompanyId();

	@Query(value = "SELECT DISTINCT COUNT(*) from vendor_company", nativeQuery = true)
	public Integer getAllVendorCompanyCount();

	public VendorCompany findFirstByOrderByIdDesc();

	@Query(value = "SELECT id FROM vendor_company WHERE referral_code=:referral_code", nativeQuery = true)
	public Integer getAllVendorCompanyByUserReferarralCode(@Param("referral_code") String referral_code_used);

	public Boolean existsByEmail(String email);

	public Boolean existsByPrimaryPhone(String primaryPhone);

}
