package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Verification_Otp;

@Repository
public interface OtpRepository extends JpaRepository<Verification_Otp,Integer>{
	
	public Verification_Otp findByPhone(String phoneNumber);
	
	public Verification_Otp findByEmail(String email);

}
