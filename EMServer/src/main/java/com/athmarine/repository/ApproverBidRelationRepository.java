package com.athmarine.repository;

import com.athmarine.entity.ApproverBidRelation;
import com.athmarine.entity.Bids;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApproverBidRelationRepository extends JpaRepository<ApproverBidRelation,Integer> {

    @Query("select count(a) from ApproverBidRelation a where bidId in :bidsIds and bidStatus =:bidStatus")
    Integer countByBidsAndBidStatus(List<Integer> bidsIds, String bidStatus);

    @Query("select a from ApproverBidRelation a where bidId in :bidsIds and bidStatus =:bidStatus")
    List<ApproverBidRelation> findByBidsAndBidStatus(List<Integer> bidsIds, String bidStatus, Pageable pageable);

    @Query("select a from ApproverBidRelation a where bidId =:bidId and bidStatus =:bidStatus")
    ApproverBidRelation findByBidAndBidStatus(Integer bidId, String bidStatus);

    @Query(value = "Select * from approver_bid_relation where approver_id=:approverId AND bid_status=:bidStatus",
            countQuery = "Select count(*) from approver_bid_relation where approver_id=:approverId AND bid_status=:bidStatus",
            nativeQuery = true)
    List<ApproverBidRelation> findByApproverIdAndBidStatus(Integer approverId, String bidStatus, Pageable pageable);

    Integer countByApproverIdAndBidStatus(Integer approverId, String bidStatus);

    ApproverBidRelation findByBidId(Integer bidId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE approver_bid_relation SET bid_status=:bidStatus, decline_reason=:declineReason WHERE id=:bidId",nativeQuery = true)
    Integer changeBidStatusById(Integer bidId,String bidStatus,String declineReason);
}
