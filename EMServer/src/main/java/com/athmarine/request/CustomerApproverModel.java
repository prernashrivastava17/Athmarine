package com.athmarine.request;

import com.athmarine.entity.CustomerApproverApprovedStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerApproverModel {
	
	private Integer id;

	private Integer userId;

	private CustomerApproverApprovedStatus isApprovedStatus;
	
	private int status;

	private UserModel approvedBy;
}
