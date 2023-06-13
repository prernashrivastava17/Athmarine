package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.VendorApproversPriceRange;

@Repository
public interface VendorApproversPriceRangeRepository extends JpaRepository<VendorApproversPriceRange,Integer>{

}
