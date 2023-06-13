package com.athmarine.request;

import java.util.Date;

import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.TermAndConditionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorCompanysModel {
	
	private Integer id;

	private Integer userId;

	private String logo;

	private String address;

	private String city_of_registration;

	private String country_of_registration;

	private String pincode;

	private String type;

	private String faxno;

	private boolean sameAsAdmin;

	private boolean registered;
	
	private String isEstablishedLastFiveYear;

	private String registrationNo;

	private int status;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date yearOfReg;

	private MasterStateModel mstateStateModel;

	private String masterCityModel;

	private MasterCountryModel masterCountryModel;

	private String token;
	
	private String companyUid;

	private RegistrationStatus registrationStatus;

	private boolean isRegisteredSuccessfully;

	private TermAndConditionStatus termAndConditionStatus;

	private String referralCodeUsed;

	private String referralCode;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date companyStablished;
	
	private String yesOrNo;

}
