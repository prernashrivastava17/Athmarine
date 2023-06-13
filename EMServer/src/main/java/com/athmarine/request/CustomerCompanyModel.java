package com.athmarine.request;

import java.util.Date;

import com.athmarine.entity.RegistrationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCompanyModel {

	private Integer id;

	private Integer userId;

	private String logo;

	private String address;

	private String city;
	
	private String companyUid;

	private String pincode;

	private String faxno;

	private boolean isAdminRegistered;

	private boolean isRegistered;

	private String registrationNo;
	
	private String city_of_registration;

	private String country_of_registration;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date yearOfReg;

	private MasterStateModel mstateStateModel;

	private String masterCity;

	private MasterCountryModel masterCountryModel;
	
	private Integer vessel;

	private int status;

	private String token;

	private RegistrationStatus registrationStatus;

	private boolean isRegisteredSuccessfully;
	
	private String referralCodeUsed;

	private String referralCode;
	
	private String customerUid;

}
