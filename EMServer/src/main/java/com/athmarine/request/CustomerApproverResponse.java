package com.athmarine.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerApproverResponse {

	private List<UserModel> userModel;
	
	private List<UserListResponseModel> requestor;

}
