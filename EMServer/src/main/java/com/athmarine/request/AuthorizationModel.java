package com.athmarine.request;

import java.util.List;

import com.athmarine.entity.RegistrationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationModel {

	private Integer userId;

	private String name;

	private String email;

	private String primaryPhone;
	
	private String phoneCode;

	private String token;

	private String type;

	private List<RoleModels> roleModel;

	private String uid;

	private Integer companyId;

	private String referralCode;

	private RegistrationStatus registrationStatus;

	private int registrationStatusKey;

	private boolean isRegisteredSuccessfully;

	private Integer userAdminId;
	
	private String individualNo;

}
