package com.athmarine.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.HeadOfCompany;
@Repository
public interface HeadOfCompanyRepository extends JpaRepository<HeadOfCompany, Integer> {
	
	@Query(value="SELECT COUNT(*) FROM head_of_company WHERE head_id=:headId AND status=:status ",nativeQuery = true)
	public Integer getCountByStatus(Integer headId, String status);
	
	@Query(value="SELECT * FROM head_of_company WHERE head_id=:headId AND status=:status ",nativeQuery = true)
	public List<HeadOfCompany> getDetails(Integer headId, String status, Pageable pageable);
	
	@Query(value="SELECT * FROM head_of_company WHERE head_id=:headId AND status=:status AND  created_at BETWEEN :startDate AND :endDate",nativeQuery = true)
	public List<HeadOfCompany> getDetailsByFilter(Integer headId, String status,Date startDate, Date endDate ,Pageable pageable);
	
	@Query(value="SELECT * FROM head_of_company WHERE head_id=:headId AND status=:status AND  service_on BETWEEN :startDate AND :endDate",nativeQuery = true)
	public List<HeadOfCompany> getDetailsByServiceFilter(Integer headId, String status,Date startDate, Date endDate ,Pageable pageable);

	@Query(value="SELECT * FROM head_of_company WHERE head_id=:headId AND status=:status AND  po_received_at BETWEEN :startDate AND :endDate",nativeQuery = true)
	public List<HeadOfCompany> getDetailsByPoDateFilter(Integer headId, String status,Date startDate, Date endDate ,Pageable pageable);
	
	public List<HeadOfCompany> findByHeadId(Integer headId);

	@Query(value = "SELECT * FROM head_of_company WHERE head_id=:headId AND `status`=:status",nativeQuery = true)
	public List<HeadOfCompany> getAllAccessRequest(Integer headId, String status);

	@Query(value= "SELECT * FROM head_of_company WHERE head_id=:headId AND status_id=:userId AND status=:status", nativeQuery = true)
	public HeadOfCompany findByHeadAndUserIdAndStatus(Integer headId, Integer userId, String status);

	@Query(value="SELECT * FROM head_of_company WHERE head_id=:headId AND status in :status ",nativeQuery = true)
	public List<HeadOfCompany> findByHeadIdAndStatus(Integer headId, List<String> status, Pageable pageable);

	@Query(value= "SELECT * FROM head_of_company WHERE status_id=:userId AND status=:status", nativeQuery = true)
	public HeadOfCompany findByUserIdAndStatus(Integer userId, String status);
	
}
