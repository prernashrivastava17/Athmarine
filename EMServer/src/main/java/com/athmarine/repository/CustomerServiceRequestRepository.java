package com.athmarine.repository;

import java.util.List;

import com.athmarine.entity.ServiceRequest;
import com.athmarine.entity.User;
import com.athmarine.request.GetNewServiceRequestsRequest;

public interface CustomerServiceRequestRepository {

	public List<ServiceRequest> findAllByRequester(User requester, GetNewServiceRequestsRequest request);

	public List<ServiceRequest> findAllServiceByRequester(User requester, GetNewServiceRequestsRequest request);
	
}
