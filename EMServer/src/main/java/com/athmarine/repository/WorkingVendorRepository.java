package com.athmarine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Bids;
import com.athmarine.entity.User;
import com.athmarine.entity.WorkingVendor;

@Repository
public interface WorkingVendorRepository extends JpaRepository<WorkingVendor, Integer> {

	public List<WorkingVendor> findAllByBids(Bids bids);
	
}
