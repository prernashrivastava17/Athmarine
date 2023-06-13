package com.athmarine.repository;

import com.athmarine.entity.ApproverBidRelation;
import com.athmarine.entity.Bids;
import com.athmarine.entity.PurchaseOrder;
import com.athmarine.entity.ServiceRequestStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    PurchaseOrder findByBid(Bids bids);

    @Query(value = "SELECT COUNT(*) FROM purchase_order WHERE bid_id=:bidId", nativeQuery = true)
    public Integer findDuplicateDateByBidId(Integer bidId);

    @Query("Select p from PurchaseOrder p where bid=:bids")
    List<PurchaseOrder> findByBids(Bids bids);

    List<PurchaseOrder> findByPoStatusAndStatus(String poStatus, Boolean status);
    @Query(value = "SELECT * FROM purchase_order WHERE id=:id", nativeQuery = true)
    public PurchaseOrder getPOById(Integer id);

    @Query("select p from PurchaseOrder p where bid in :bids and poStatus =:poStatus")
    List<PurchaseOrder> findByBidsAndPOStatus(List<Bids> bids, String poStatus, Pageable pageable);

    @Query("select count(p) from PurchaseOrder p where bid in :bids and poStatus =:poStatus")
    Integer countByBidsAndPOStatus(List<Bids> bids, String poStatus);
    @Query(value = "SELECT * FROM purchase_order WHERE service_request_id=:serviceRequestId",nativeQuery = true)
    PurchaseOrder findByRequestId(Integer serviceRequestId);
}
