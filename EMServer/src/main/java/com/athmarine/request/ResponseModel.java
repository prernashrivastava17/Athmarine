package com.athmarine.request;

import java.util.List;

import lombok.Data;

@Data
public class ResponseModel {

	private UserModel approver;
	private List<UserListResponseModel> requesterIds;
	
}
