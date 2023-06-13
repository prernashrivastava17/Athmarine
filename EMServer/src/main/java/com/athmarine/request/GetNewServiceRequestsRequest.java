package com.athmarine.request;

import com.athmarine.entity.ServiceRequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetNewServiceRequestsRequest {

	int requesterId;

	int page;

	int size;

	ServiceRequestStatus requestStatus;
}
