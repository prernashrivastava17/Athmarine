package com.athmarine.repository;

import java.util.List;

import com.athmarine.entity.ServiceRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.ServiceRequest;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.request.GetNewServiceRequestsRequest;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Integer> {

	public List<ServiceRequest> findAllByRequester(User requester, GetNewServiceRequestsRequest request);

	public List<ServiceRequest> findAllByRequester(User requester);

	public Page<ServiceRequest> findAllByRequester(User requester, Pageable pageable);

//	public List<ServiceRequest> findAllByVendorCompany(VendorCompany company);

	public List<ServiceRequest> findByRequestStatus(String serviceRequestStatus);

	public int countByRequestStatus(String status);

//	public List<ServiceRequest> findAllByVendorCompany(VendorCompany company);
	
	@Query(value="Select * from service_request where id=:id" ,nativeQuery =  true)
	public ServiceRequest findByRequestId(Integer id);
}
