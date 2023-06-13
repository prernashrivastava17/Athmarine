package com.athmarine.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.BidEngineerRelation;
@Repository
public interface EngineerBidRelationRepository extends JpaRepository<BidEngineerRelation,  Integer> {
	
	public List<BidEngineerRelation> findByBidId(Integer bidId);

	public List<BidEngineerRelation> findByEngineerStatus(String engineerStatus);
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM engineer_bid_relation WHERE bid_id=:id", nativeQuery = true)
	public void deleteByBidId(Integer id);
	
	@Query(value = "SELECT COUNT(*) FROM engineer_bid_relation WHERE engineer_status=:status AND engineer_id=:engineerId", nativeQuery = true)
	public Integer findByEngineerIdAndEngineerStatus(Integer engineerId,String status);
	
	@Query(value = "SELECT COUNT(*) FROM engineer_bid_relation WHERE service_report IS NOT NULL AND engineer_id=:engineerId", nativeQuery = true)
	public Integer getServiceReportUploadedCount(Integer engineerId);
	
	@Query(value="SELECT * FROM engineer_bid_relation WHERE engineer_id=:engineerId AND service_request_id=:serviceRequestId",nativeQuery= true)
	public BidEngineerRelation findJobByEngineerIdServiceId(Integer engineerId,Integer serviceRequestId);

	@Query(value="SELECT COUNT(*) FROM engineer_bid_relation WHERE engineer_id=:engineerId AND service_request_id=:serviceRequestId",nativeQuery= true)
	public Integer checkDuplicatesByEngineerIdServiceId(Integer engineerId,Integer serviceRequestId);
	
	@Query(value = "SELECT * FROM engineer_bid_relation WHERE service_report IS NOT NULL AND engineer_id=:engineerId", nativeQuery = true)
	public List<BidEngineerRelation> findAllJobsByEngineerId(Integer engineerId);
	
	@Query(value = "SELECT * FROM engineer_bid_relation WHERE engineer_status=:status AND engineer_id=:engineerId", nativeQuery = true)
	public List<BidEngineerRelation> findAllJobs(Integer engineerId,String status);
	
	
	@Query(value = "SELECT COUNT(*) FROM engineer_bid_relation WHERE engineer_id=:engineerId", nativeQuery = true)
	public Integer checkEngineerIdExist(Integer engineerId);
	
	@Query( value = "SELECT COUNT(*) FROM engineer_bid_relation WHERE service_request_id=:serviceRequestId",nativeQuery = true)
	public Integer checkRequestIdExist(Integer serviceRequestId);
	
	@Query(value = "SELECT service_report FROM engineer_bid_relation WHERE service_request_id=:serviceRequestId AND service_report IS NOT NULL", nativeQuery = true)
	public String findServiceReportByServiceRequest(Integer serviceRequestId);

	@Query(value = "SELECT * FROM engineer_bid_relation WHERE engineer_id=:engineerId AND engineer_status IN (\"ASSIGNED\",\"JOB_IN_PROGRESS\") AND MONTH(service_on)=:month",nativeQuery = true)
	public List<BidEngineerRelation> getAllJObsByEngineerIdAndServiceMonth(Integer engineerId,Integer month);

	@Query(value = "SELECT * FROM engineer_bid_relation WHERE engineer_id=:engineerId AND engineer_status IN (\"ASSIGNED\") AND service_on=:serviceDate",nativeQuery = true)
	public List<BidEngineerRelation> getJObDetailByEngineerIdAndServiceDate(Integer engineerId, String serviceDate);

	@Query(value = "SELECT * FROM engineer_bid_relation WHERE bid_id=:bidId AND engineer_id=:engineerId AND  service_request_id=:serviceRequestId",nativeQuery = true)
	public BidEngineerRelation getEngineersByBidIdEngineerIDServiceId(Integer bidId,Integer engineerId,Integer serviceRequestId);
}
