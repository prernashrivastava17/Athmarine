package com.athmarine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.SparesVendor;
import com.athmarine.entity.User;

@Repository
public interface SparesVendorRepository extends JpaRepository<SparesVendor, Integer>{

	public Optional<SparesVendor> getSparesVendorBybidderId(User user);

}
