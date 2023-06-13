package com.athmarine.request;

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
public class CompanyHeadModel {
	
	private Integer id;

	private String name;

	private String email;

	private String type;

	private String primaryPhone;

	private String phoneCode;

	private String secondaryPhone;

	private String designation;

	private String uid;

	private boolean phoneVerified;

	private boolean emailVerified;

	private boolean onWhatsapp;

	private String password;
	
	private String currency;

	private int status;

	private String token;

	private RoleModels roleModel;

	private Integer companyId;

	private String biddingLimit;

	private Integer bidderId;

	private Integer approverId;

	private String availableOn;
	
	private String dob;
	
	private String imageUrl;
	
	private boolean termAndConditionAccepted;

}
