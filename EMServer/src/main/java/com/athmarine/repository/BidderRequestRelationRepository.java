package com.athmarine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.BidderRequestRelation;

import java.util.List;

@Repository
public interface BidderRequestRelationRepository extends JpaRepository<BidderRequestRelation, Integer> {

	public int countByBidderId(Integer bidderId);
	
	@Query(value = "SELECT * FROM bidder_request_relation WHERE bid_id=:bidId" , nativeQuery = true)
	public BidderRequestRelation findByBidId(Integer bidId);

	@Query(value = "SELECT * FROM bidder_request_relation WHERE bidder_id=:bidderId AND request_id=:requestId AND request_status=:status", nativeQuery = true)
	public BidderRequestRelation findByBidderIdAndRequestId(Integer bidderId, Integer requestId, String status);

    List<BidderRequestRelation> findByBidderIdAndRequestStatus(Integer bidderId, String requestStatus, Pageable pageable);

	Integer countByBidderIdAndRequestStatus(Integer bidderId, String requestStatus);

	BidderRequestRelation findByBidderIdAndRequestIdAndRequestStatus(Integer bidderId, Integer requestId, String requestStatus);

    List<BidderRequestRelation> findByRequestIdAndRequestStatus(Integer requestId, String requestStatus);
}
