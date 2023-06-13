package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.ServiceRequestStatus;
import com.athmarine.response.RequesterDashboardResonseModel;
import com.athmarine.response.ServiceRequestResponseModel;

@Service
public class DashBoardService {

	@Autowired
	ServiceRequestService serviceRequestService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	public RequesterDashboardResonseModel getRequesterDashboard(@NotBlank int id) {
		RequesterDashboardResonseModel response = new RequesterDashboardResonseModel();
		List<ServiceRequestResponseModel> serviceRequest = serviceRequestService.getAllServiceRequestByRequesterId(id);
		int openRequest = (int) serviceRequest.stream()
				.filter(request -> request.getRequestStatus().equals(ServiceRequestStatus.NEW_REQUEST)).count();
		response.setOpenRequest(openRequest);

		int bidsReceived = (int) serviceRequest.stream()
				.filter(request -> request.getRequestStatus().equals(ServiceRequestStatus.BIDS_RECEIVED)).count();
		response.setBidsReceived(bidsReceived);

		int completedJobs = (int) serviceRequest.stream()
				.filter(request -> request.getRequestStatus().equals(ServiceRequestStatus.COMPLETED)).count();
		response.setCompletedJobs(completedJobs);

		int pendingForApprovalRequest=(int) serviceRequest.stream()
				.filter(request-> request.getRequestStatus().equals(ServiceRequestStatus.APPROVER_PENDING)).count();
		response.setPendingForApprovalRequest(pendingForApprovalRequest);

		response.setLatestRequest(serviceRequest.stream().limit(4).collect(Collectors.toList()));
		return response;

	}

}
