package com.athmarine.repository;

import com.athmarine.entity.Bids;
import com.athmarine.entity.ServiceRequestInvoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestInvoiceRepository extends JpaRepository<ServiceRequestInvoice,Integer> {

    ServiceRequestInvoice findByBid(Bids bid);

    @Query("select i from ServiceRequestInvoice i where i.bid in :bids")
    List<ServiceRequestInvoice> findAllByBids(List<Bids> bids, Pageable pageable);

    @Query("select count(i) from ServiceRequestInvoice i where i.bid in :bids")
    Integer countAllByBids(List<Bids> bids);

    @Query("select count(i) from ServiceRequestInvoice i where i.bid in :bids and invoiceStatus=:status")
    Integer countByBidAndInvoiceStatus(List<Bids> bids, String status);

    @Query("select i from ServiceRequestInvoice i where i.bid in :bids and invoiceStatus=:status")
    List<ServiceRequestInvoice> findByBidAndInvoiceStatus(List<Bids> bids, String status, Pageable pageable);
    
    @Query(value = "SELECT purchase_order_id FROM service_request_invoice WHERE service_request_id=:serviceRequest", nativeQuery = true)
    Integer findPoIdByServiceRequest(Integer serviceRequest);

    @Query(value = "SELECT * FROM service_request_invoice WHERE service_request_id=:serviceRequestId", nativeQuery = true)
    ServiceRequestInvoice findByServiceRequest(Integer serviceRequestId);

}
