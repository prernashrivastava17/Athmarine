package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.BidderApprover;

@Repository
public interface BidderApproverRepository extends JpaRepository<BidderApprover, Integer> {

}
