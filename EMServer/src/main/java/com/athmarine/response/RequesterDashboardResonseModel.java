package com.athmarine.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequesterDashboardResonseModel {

	private Integer openRequest;

	private Integer bidsReceived;

	private Integer pendingForApprovalRequest;

	private Integer completedJobs;

	private List<ServiceRequestResponseModel> latestRequest;
}
