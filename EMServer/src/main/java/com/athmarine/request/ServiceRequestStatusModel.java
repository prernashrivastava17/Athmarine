package com.athmarine.request;

import com.athmarine.entity.ServiceRequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestStatusModel {
	
    private Integer id;
	
	private ServiceRequestStatus requestStatus;

}
