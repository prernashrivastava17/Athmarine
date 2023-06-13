package com.athmarine.service;

import com.athmarine.request.CustomerApproverModel;
import com.athmarine.request.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerApproverUserModel {

	private UserModel approver;

	private CustomerApproverModel customerApproverModel;
	
}
