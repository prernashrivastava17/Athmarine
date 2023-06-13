package com.athmarine.request;

import com.athmarine.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyModel {

	private Integer id;
	
	private String address;

	private String name;

	private String email;

	private String type;

	private String primaryPhone;
	
	private String phoneCode;
	
	private String currency;

	private String secondaryPhone;

	private String designation;

	private boolean isPhoneVerified;

	private boolean isEmailVerified;

	private boolean isOnWhatsapp;
	
	private boolean termAndConditionAccepted;
	
	private boolean emailVerifiedTerm;

	private Role role;

	private String uid;

	private Integer companyId;

	private Integer status;

	private String availableOn;
	
	private String individualNo;

}
