package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Bids;
import com.athmarine.entity.TravelCostVendor;

@Repository
public interface TravelCostVendorRepository extends JpaRepository<TravelCostVendor, Integer> {

	//public Optional<TravelCostVendor>  getTravelCostVendorBybidderId(User bidderId);
	
	public List<TravelCostVendor> findAllTravelByBids(Bids bids);
}
