package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Bids;
import com.athmarine.entity.MiscellaneousVendor;

@Repository
public interface MiscellaneousVendorRepository extends JpaRepository<MiscellaneousVendor, Integer>{

//	public Optional<MiscellaneousVendor> getMiscellaneousVendorBybidderId(User user);
	
	public List<MiscellaneousVendor> findAllMiscellaneousByBids(Bids bids);

}
