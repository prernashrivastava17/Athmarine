package com.athmarine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailPhoneResponseModel {

	private Integer id;

	private String email;

	private String primaryPhone;
	
	private String phoneCode;
	
	private boolean emailVerified;
	
	private boolean phoneVerified;
	
	private String isPasswordExist;
}
