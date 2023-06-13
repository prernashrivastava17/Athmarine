package com.athmarine.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Bids;
import com.athmarine.entity.ServiceRequest;
import com.athmarine.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BidsRepository extends JpaRepository<Bids, Integer> {

	int countByUserIdAndBidsStatusAndStatus(User user, String bidsStatus, Boolean status);

	int countByUserIdAndAdditionalCostStatusAndStatus(User user, String addCostStatus, Boolean status);

	@Query(value = "Select * from bids where bidder_id=:id AND bids_status=:bidStatus AND status=:status",
			countQuery = "Select count(*) from bids where bidder_id=:id AND bids_status=:bidStatus AND status=:status",
			nativeQuery = true)
	List<Bids> findByBidderId(Integer id, String bidStatus, Boolean status, Pageable pageable);
	
	@Query(value = "SELECT * FROM bids WHERE service_request_id=:requestId AND company_id=:companyId", nativeQuery = true)
	List<Bids> findSequenceNumber(Integer requestId, Integer companyId);

	@Query(value = "Select * from bids where bidder_id=:bidderId AND service_request_id=:requestId AND bids_status=:bidStatus AND status=true", nativeQuery = true)
	List<Bids> findByBidderAndRequestId(Integer bidderId,Integer requestId,String bidStatus);

	@Query (value = "select * from bids where service_request_id=:id AND bids_status=:bidStatus AND status=true", nativeQuery = true)
	List<Bids> findBidsByRequestIdAndBidStatus(Integer id,String bidStatus);

	@Query (value = "select count(*) from bids where service_request_id=:id AND bids_status=:bidStatus AND status=true", nativeQuery = true)
	Integer countByRequestIdAndBidStatus(Integer id,String bidStatus);

	List<Bids> findByUserIdAndBidsStatusAndStatus(User bidder, String bidsStatus, Boolean status, Pageable pageable);

	@Query(value = "SELECT * FROM bids where bidder_id=:bidderId AND status = true AND additional_cost_status = \"RAISED\" \n" +
			" OR additional_cost_status=\"RECEIVED\" OR additional_cost_status=\"REVOKED\""
			,countQuery = "SELECT * FROM bids where bidder_id=:bidderId AND status = true AND additional_cost_status = \\\"RAISED\\\" \\n\" +\n" +
			"\t\t\t\" OR additional_cost_status=\\\"RECEIVED\\\" OR additional_cost_status=\\\"REVOKED\\\"" ,nativeQuery = true)
	List<Bids> findAdditionalCostRaisedBids(Integer bidderId, Pageable pageable);


	@Query("select b from Bids b where userId in :bidders")
	List<Bids> findAllBiddersBids(List<User> bidders);
	
	@Query(value = "SELECT COUNT(*) FROM bids WHERE company_id=:companyId AND bids_status=\"HOC_APPROVAL_PENDING\"", nativeQuery = true)
	Integer getHocApprovalCount(Integer companyId);

	@Query(value = "Select count(*) from bids where bidder_id=:id AND bids_status=:bidStatus", nativeQuery = true)
	Integer countByBidderIdAndStatus(Integer id, String bidStatus);

	List<Bids> findByUserId(User bidder);

	@Query(value = "Select * from bids where bidder_id=:id AND bids_status in :bidStatus AND status=true", nativeQuery = true)
	List<Bids> findByBidderId(Integer id, List<String> bidStatus);

	@Transactional
	@Modifying
	@Query(value = "UPDATE bids SET bids_status=:bidStatus WHERE id=:bidId",nativeQuery = true)
	Integer changeBidStatusById(Integer bidId,String bidStatus);

	@Query(value = "Select * from bids where id=:id", nativeQuery = true)
	public Bids findByIDs(Integer id);

}
